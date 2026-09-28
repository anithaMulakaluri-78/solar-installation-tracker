package com.solartracker.controller;

import com.solartracker.dto.request.DcrStatusUpdateRequest;
import com.solartracker.dto.request.InstallationRequest;
import com.solartracker.dto.response.InstallationResponse;
import com.solartracker.dto.response.PageResponse;
import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import com.solartracker.service.InstallationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/installations")
@RequiredArgsConstructor
@Tag(name = "Installations")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')")
public class InstallationController {

    private final InstallationService installationService;

    @GetMapping
    @Operation(summary = "List installations - search/filter by DCR status, workflow status, section, pagination")
    public ResponseEntity<PageResponse<InstallationResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) DcrStatus dcrStatus,
            @RequestParam(required = false) InstallationStatus status,
            @RequestParam(required = false) String section,
            Pageable pageable) {
        return ResponseEntity.ok(installationService.list(search, dcrStatus, status, section, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(installationService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<InstallationResponse> create(@Valid @RequestBody InstallationRequest request) {
        return ResponseEntity.status(201).body(installationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')")
    public ResponseEntity<InstallationResponse> update(@PathVariable Long id, @Valid @RequestBody InstallationRequest request) {
        return ResponseEntity.ok(installationService.update(id, request));
    }

    // Module 2: DCR tracking - a separate, focused action rather than a full record edit,
    // since in practice this is done by ADMIN/SUPER_ADMIN reviewing uploaded certificates,
    // not by the installer who submitted the site.
    @PatchMapping("/{id}/dcr-status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @Operation(summary = "Update the DCR compliance status for an installation")
    public ResponseEntity<InstallationResponse> updateDcrStatus(@PathVariable Long id, @Valid @RequestBody DcrStatusUpdateRequest request) {
        return ResponseEntity.ok(installationService.updateDcrStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        installationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
