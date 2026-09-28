package com.solartracker.controller;

import com.solartracker.dto.request.*;
import com.solartracker.dto.response.*;
import com.solartracker.entity.*;
import com.solartracker.entity.enums.InstallationStatus;
import com.solartracker.exception.DuplicateResourceException;
import com.solartracker.exception.ResourceNotFoundException;
import com.solartracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/installations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')")
public class InstallationWorkflowController {
    private final InstallationRepository installationRepository;
    private final InstallationPanelRepository panelRepository;
    private final InstallationInverterRepository inverterRepository;
    private final InstallationVerificationRepository verificationRepository;
    private final InstallationLocationRepository locationRepository;
    private final InstallationPhotoRepository photoRepository;
    private final InstallationSubmissionRepository submissionRepository;
    private final DcrRecordRepository dcrRepository;
    private final DcrDocumentRepository documentRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}/panels") public List<PanelView> panels(@PathVariable Long id){
        findInstallation(id); return panelRepository.findAll().stream().filter(p->p.getInstallation().getId().equals(id))
                .sorted(Comparator.comparing(InstallationPanel::getPanelPosition, Comparator.nullsLast(Integer::compareTo))).map(p->new PanelView(p.getId(),p.getPanelPosition(),p.getPanelSerialNumber())).toList();
    }
    @PostMapping("/{id}/panels") @PreAuthorize("hasAnyRole('ADMIN','INSTALLER','SUPER_ADMIN')") public ResponseEntity<PanelView> addPanel(@PathVariable Long id,@Valid @RequestBody PanelRequest r){
        Installation i=findInstallation(id); if(panelRepository.findByPanelSerialNumber(r.panelSerialNumber()).isPresent()) throw new DuplicateResourceException("Panel serial already exists: "+r.panelSerialNumber());
        InstallationPanel p=InstallationPanel.builder().installation(i).panelPosition(r.panelPosition()).panelSerialNumber(r.panelSerialNumber()).build(); return ResponseEntity.status(201).body(new PanelView(panelRepository.save(p).getId(),p.getPanelPosition(),p.getPanelSerialNumber()));
    }
    @DeleteMapping("/{id}/panels/{panelId}") public ResponseEntity<Void> deletePanel(@PathVariable Long id,@PathVariable Long panelId){InstallationPanel p=panelRepository.findById(panelId).orElseThrow(()->new ResourceNotFoundException("Panel not found: "+panelId)); if(!p.getInstallation().getId().equals(id)) throw new ResourceNotFoundException("Panel does not belong to installation"); panelRepository.delete(p); return ResponseEntity.noContent().build();}

    @GetMapping("/{id}/inverter") public InverterResponse inverter(@PathVariable Long id){ InstallationInverter x=inverterRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("Inverter not captured")); return inverter(x); }
    @PostMapping("/{id}/inverter") public ResponseEntity<InverterResponse> addInverter(@PathVariable Long id,@Valid @RequestBody InverterRequest r){ Installation i=findInstallation(id); if(inverterRepository.findByInstallationId(id).isPresent()) throw new DuplicateResourceException("Inverter already captured for installation"); if(inverterRepository.existsByInverterSerialNumber(r.getInverterSerialNumber())) throw new DuplicateResourceException("Inverter serial already exists: "+r.getInverterSerialNumber()); InstallationInverter x=InstallationInverter.builder().installation(i).inverterSerialNumber(r.getInverterSerialNumber()).inverterMake(r.getInverterMake()).inverterModel(r.getInverterModel()).inverterCapacityKw(r.getInverterCapacityKw()).build(); i.setInverterSerialNumber(r.getInverterSerialNumber()); installationRepository.save(i); return ResponseEntity.status(201).body(inverter(inverterRepository.save(x))); }
    @PutMapping("/{id}/inverter") public InverterResponse updateInverter(@PathVariable Long id,@Valid @RequestBody InverterRequest r){ InstallationInverter x=inverterRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("Inverter not captured")); x.setInverterSerialNumber(r.getInverterSerialNumber());x.setInverterMake(r.getInverterMake());x.setInverterModel(r.getInverterModel());x.setInverterCapacityKw(r.getInverterCapacityKw()); Installation i=findInstallation(id);i.setInverterSerialNumber(r.getInverterSerialNumber());installationRepository.save(i);return inverter(inverterRepository.save(x)); }

    @GetMapping("/{id}/verification") public VerificationResponse verification(@PathVariable Long id){ InstallationVerification v=verificationRepository.findTopByInstallationIdOrderByVerifiedAtDesc(id).orElseThrow(()->new ResourceNotFoundException("Verification not found")); return verification(v); }
    @PostMapping("/{id}/verification") public VerificationResponse verify(@PathVariable Long id,@Valid @RequestBody VerificationRequest r,Authentication auth){ Installation i=findInstallation(id); User u=currentUser(auth); InstallationVerification v=InstallationVerification.builder().installation(i).verifiedBy(u).verificationStatus(r.getVerificationStatus()).verificationRemarks(r.getVerificationRemarks()).verifiedAt(LocalDateTime.now()).build(); if("VERIFIED".equalsIgnoreCase(r.getVerificationStatus())) i.setStatus(InstallationStatus.IN_PROGRESS); installationRepository.save(i); return verification(verificationRepository.save(v)); }

    @GetMapping("/{id}/location") public LocationResponse location(@PathVariable Long id){InstallationLocation l=locationRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("Location not captured"));return location(l);}
    @PostMapping("/{id}/location") public LocationResponse saveLocation(@PathVariable Long id,@Valid @RequestBody LocationRequest r,Authentication auth){Installation i=findInstallation(id); InstallationLocation l=locationRepository.findByInstallationId(id).orElse(InstallationLocation.builder().installation(i).build());l.setLatitude(r.getLatitude());l.setLongitude(r.getLongitude());l.setAccuracyMeters(r.getAccuracyMeters());l.setCapturedAt(LocalDateTime.now());l.setCapturedBy(currentUser(auth));return location(locationRepository.save(l));}

    @GetMapping("/{id}/photos") public List<PhotoResponse> photos(@PathVariable Long id){findInstallation(id);return photoRepository.findByInstallationIdOrderByCapturedAtDesc(id).stream().map(this::photo).toList();}
    @PostMapping(value="/{id}/photos", consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public ResponseEntity<PhotoResponse> uploadPhoto(@PathVariable Long id,@RequestParam("photo") MultipartFile file,@RequestParam String photoType,@RequestParam(required=false) BigDecimal latitude,@RequestParam(required=false) BigDecimal longitude,Authentication auth) throws IOException {
        Installation i=findInstallation(id); if(file.isEmpty()) throw new IllegalArgumentException("Photo is empty"); String clean=StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())); Path dir=Paths.get("uploads","installations",String.valueOf(id));Files.createDirectories(dir);String name=UUID.randomUUID()+"-"+clean;Path target=dir.resolve(name);Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);InstallationPhoto p=InstallationPhoto.builder().installation(i).photoType(photoType).fileName(clean).filePath("/uploads/installations/"+id+"/"+name).latitude(latitude).longitude(longitude).capturedAt(LocalDateTime.now()).uploadedAt(LocalDateTime.now()).capturedBy(currentUser(auth)).status("UPLOADED").build();return ResponseEntity.status(201).body(photo(photoRepository.save(p))); }
    @DeleteMapping("/{id}/photos/{photoId}") public ResponseEntity<Void> deletePhoto(@PathVariable Long id,@PathVariable Long photoId){InstallationPhoto p=photoRepository.findById(photoId).orElseThrow(()->new ResourceNotFoundException("Photo not found"));if(!p.getInstallation().getId().equals(id))throw new ResourceNotFoundException("Photo does not belong to installation");photoRepository.delete(p);return ResponseEntity.noContent().build();}

    @GetMapping("/{id}/review") public ReviewResponse review(@PathVariable Long id){Installation i=findInstallation(id);boolean verified=verificationRepository.findTopByInstallationIdOrderByVerifiedAtDesc(id).map(v->"VERIFIED".equalsIgnoreCase(v.getVerificationStatus())).orElse(false);boolean inv=inverterRepository.findByInstallationId(id).isPresent();boolean loc=locationRepository.findByInstallationId(id).isPresent();int pc=(int)panelRepository.findAll().stream().filter(p->p.getInstallation().getId().equals(id)).count();int photos=(int)photoRepository.findByInstallationIdOrderByCapturedAtDesc(id).size();return ReviewResponse.builder().installationId(id).consumerVerified(verified).inverterCaptured(inv).locationCaptured(loc).panelCount(pc).requiredPhotoCount(6).uploadedPhotoCount(photos).readyForSubmission(verified&&inv&&loc&&pc>0&&photos>=6).build();}
    @PostMapping("/{id}/submit") public ResponseEntity<ReviewResponse> submit(@PathVariable Long id,@RequestBody(required=false) SubmitRequest r,Authentication auth){Installation i=findInstallation(id);ReviewResponse review=review(id);if(!review.isReadyForSubmission()) throw new IllegalStateException("Installation is not ready for submission");i.setStatus(InstallationStatus.SUBMITTED);installationRepository.save(i);submissionRepository.save(InstallationSubmission.builder().installation(i).submittedBy(currentUser(auth)).submissionStatus("SUBMITTED").remarks(r==null?null:r.getRemarks()).submittedAt(LocalDateTime.now()).build());return ResponseEntity.ok(review(id));}

    @GetMapping("/{id}/dcr") public DcrResponse dcr(@PathVariable Long id){DcrRecord d=dcrRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("DCR record not found"));return dcr(d);}

    @PostMapping(value="/{id}/dcr/documents", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Map<String,Object>> uploadDcrDocument(@PathVariable Long id,@RequestParam String documentType,@RequestParam("file") MultipartFile file,Authentication auth) throws IOException {
        Installation installation=findInstallation(id);
        DcrRecord d=dcrRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("Create the DCR record before uploading documents"));
        if(file.isEmpty()) throw new IllegalArgumentException("Document is empty");
        Path dir=Paths.get("uploads","dcr",String.valueOf(id)); Files.createDirectories(dir);
        String clean=StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())); String name=UUID.randomUUID()+"-"+clean; Path target=dir.resolve(name); Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);
        DcrDocument doc=DcrDocument.builder().dcr(d).documentType(documentType).fileName(clean).filePath("/uploads/dcr/"+id+"/"+name).uploadedBy(currentUser(auth)).uploadedAt(LocalDateTime.now()).build();
        doc=documentRepository.save(doc);
        return ResponseEntity.status(201).body(Map.of("id",doc.getId(),"documentType",doc.getDocumentType(),"fileName",doc.getFileName(),"filePath",doc.getFilePath()));
    }

    @GetMapping("/{id}/dcr/documents") @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','INSTALLER')")
    public List<Map<String,Object>> dcrDocuments(@PathVariable Long id){
        DcrRecord d=dcrRepository.findByInstallationId(id).orElseThrow(()->new ResourceNotFoundException("DCR record not found"));
        return documentRepository.findByDcrIdOrderByUploadedAtDesc(d.getId()).stream().map(x->Map.<String,Object>of("id",x.getId(),"documentType",x.getDocumentType(),"fileName",x.getFileName(),"filePath",x.getFilePath(),"uploadedAt",x.getUploadedAt())).toList();
    }
    @PostMapping("/{id}/dcr") @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')") public ResponseEntity<DcrResponse> createDcr(@PathVariable Long id,@RequestBody DcrRequest r){Installation i=findInstallation(id);DcrRecord d=dcrRepository.findByInstallationId(id).orElse(DcrRecord.builder().installation(i).createdAt(LocalDateTime.now()).build());d.setDcrNumber(r.getDcrNumber());d.setDcrStatus(r.getDcrStatus()==null?com.solartracker.entity.enums.DcrStatus.PENDING:r.getDcrStatus());d.setRemarks(r.getRemarks());if(d.getDcrStatus()==com.solartracker.entity.enums.DcrStatus.UPLOADED)d.setSubmittedAt(LocalDateTime.now());if(d.getDcrStatus()==com.solartracker.entity.enums.DcrStatus.VERIFIED)d.setVerifiedAt(LocalDateTime.now());i.setDcrStatus(d.getDcrStatus());installationRepository.save(i);return ResponseEntity.status(201).body(dcr(dcrRepository.save(d)));}
    @PatchMapping("/{id}/dcr") @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')") public DcrResponse updateDcr(@PathVariable Long id,@RequestBody DcrRequest r){return createDcr(id,r).getBody();}

    private Installation findInstallation(Long id){return installationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Installation not found: "+id,"INSTALLATION_NOT_FOUND"));}
    private User currentUser(Authentication a){return userRepository.findByUsername(a.getName()).orElseThrow(()->new ResourceNotFoundException("User not found"));}
    private InverterResponse inverter(InstallationInverter x){return InverterResponse.builder().id(x.getId()).inverterSerialNumber(x.getInverterSerialNumber()).inverterMake(x.getInverterMake()).inverterModel(x.getInverterModel()).inverterCapacityKw(x.getInverterCapacityKw()).createdAt(x.getCreatedAt()).build();}
    private VerificationResponse verification(InstallationVerification v){return VerificationResponse.builder().id(v.getId()).verificationStatus(v.getVerificationStatus()).verificationRemarks(v.getVerificationRemarks()).verifiedBy(new UserSummaryResponse(v.getVerifiedBy().getId(),v.getVerifiedBy().getUsername(),v.getVerifiedBy().getFullName())).verifiedAt(v.getVerifiedAt()).build();}
    private LocationResponse location(InstallationLocation l){return LocationResponse.builder().id(l.getId()).latitude(l.getLatitude()).longitude(l.getLongitude()).accuracyMeters(l.getAccuracyMeters()).capturedBy(new UserSummaryResponse(l.getCapturedBy().getId(),l.getCapturedBy().getUsername(),l.getCapturedBy().getFullName())).capturedAt(l.getCapturedAt()).build();}
    private PhotoResponse photo(InstallationPhoto p){return PhotoResponse.builder().id(p.getId()).photoType(p.getPhotoType()).fileName(p.getFileName()).filePath(p.getFilePath()).latitude(p.getLatitude()).longitude(p.getLongitude()).capturedAt(p.getCapturedAt()).uploadedAt(p.getUploadedAt()).status(p.getStatus()).build();}
    private DcrResponse dcr(DcrRecord d){return DcrResponse.builder().id(d.getId()).installationId(d.getInstallation().getId()).dcrNumber(d.getDcrNumber()).dcrStatus(d.getDcrStatus()).remarks(d.getRemarks()).submittedAt(d.getSubmittedAt()).verifiedAt(d.getVerifiedAt()).build();}

    public record PanelRequest(Integer panelPosition,String panelSerialNumber){}
    public record PanelView(Long id,Integer panelPosition,String panelSerialNumber){}
}
