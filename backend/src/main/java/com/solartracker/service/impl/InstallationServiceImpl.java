package com.solartracker.service.impl;

import com.solartracker.dto.request.DcrStatusUpdateRequest;
import com.solartracker.dto.request.InstallationRequest;
import com.solartracker.dto.response.InstallationResponse;
import com.solartracker.dto.response.PageResponse;
import com.solartracker.entity.Installation;
import com.solartracker.entity.InstallationPanel;
import com.solartracker.entity.Consumer;
import com.solartracker.repository.ConsumerRepository;
import com.solartracker.entity.User;
import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import com.solartracker.exception.DuplicateResourceException;
import com.solartracker.exception.ResourceNotFoundException;
import com.solartracker.mapper.InstallationMapper;
import com.solartracker.repository.InstallationPanelRepository;
import com.solartracker.repository.InstallationRepository;
import com.solartracker.repository.UserRepository;
import com.solartracker.repository.spec.InstallationSpecifications;
import com.solartracker.service.InstallationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallationServiceImpl implements InstallationService {

    private final InstallationRepository installationRepository;
    private final InstallationPanelRepository panelRepository;
    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final InstallationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InstallationResponse> list(String search, DcrStatus dcrStatus, InstallationStatus status, String section, Pageable pageable) {
        Specification<Installation> spec = Specification
                .where(InstallationSpecifications.search(search))
                .and(InstallationSpecifications.hasDcrStatus(dcrStatus))
                .and(InstallationSpecifications.hasStatus(status))
                .and(InstallationSpecifications.hasSection(section));

        Page<Installation> page = installationRepository.findAll(spec, pageable);
        return PageResponse.of(page.map(mapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public InstallationResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional
    public InstallationResponse create(InstallationRequest request) {
        assertNoDuplicateKeys(request.getUlaApplicationId(), request.getServiceNumber(), null);

        Consumer consumer = resolveConsumer(request.getConsumerId());
        Installation installation = Installation.builder()
                .consumer(consumer)
                .ulaApplicationId(request.getUlaApplicationId())
                .section(request.getSection())
                .distribution(request.getDistribution())
                .serviceNumber(request.getServiceNumber())
                .consumerName(request.getConsumerName() != null ? request.getConsumerName() : (consumer == null ? null : consumer.getName()))
                .consumerMobile(request.getConsumerMobile() != null ? request.getConsumerMobile() : (consumer == null ? null : consumer.getMobile()))
                .newMobile(request.getNewMobile())
                .inverterSerialNumber(request.getInverterSerialNumber())
                .siteType(request.getSiteType())
                .remarks(request.getRemarks())
                .assignedInstaller(resolveUser(request.getAssignedInstallerId()))
                .build();

        applyPanelSerials(installation, request.getPanelSerialNumbers());

        return mapper.toResponse(installationRepository.save(installation));
    }

    @Override
    @Transactional
    public InstallationResponse update(Long id, InstallationRequest request) {
        Installation installation = findEntity(id);
        assertNoDuplicateKeys(request.getUlaApplicationId(), request.getServiceNumber(), id);

        Consumer consumer = resolveConsumer(request.getConsumerId());
        installation.setConsumer(consumer);
        installation.setUlaApplicationId(request.getUlaApplicationId());
        installation.setSection(request.getSection());
        installation.setDistribution(request.getDistribution());
        installation.setServiceNumber(request.getServiceNumber());
        installation.setConsumerName(request.getConsumerName() != null ? request.getConsumerName() : (consumer == null ? installation.getConsumerName() : consumer.getName()));
        installation.setConsumerMobile(request.getConsumerMobile() != null ? request.getConsumerMobile() : (consumer == null ? installation.getConsumerMobile() : consumer.getMobile()));
        installation.setNewMobile(request.getNewMobile());
        installation.setInverterSerialNumber(request.getInverterSerialNumber());
        installation.setSiteType(request.getSiteType());
        installation.setRemarks(request.getRemarks());
        installation.setAssignedInstaller(resolveUser(request.getAssignedInstallerId()));

        if (request.getPanelSerialNumbers() != null) {
            applyPanelSerials(installation, request.getPanelSerialNumbers());
        }

        return mapper.toResponse(installationRepository.save(installation));
    }

    @Override
    @Transactional
    public InstallationResponse updateDcrStatus(Long id, DcrStatusUpdateRequest request) {
        Installation installation = findEntity(id);
        installation.setDcrStatus(request.getDcrStatus());
        if (request.getRemarks() != null && !request.getRemarks().isBlank()) {
            String existing = installation.getRemarks();
            installation.setRemarks(existing == null || existing.isBlank() ? request.getRemarks() : existing + "\n" + request.getRemarks());
        }
        return mapper.toResponse(installationRepository.save(installation));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!installationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Installation not found: " + id, "INSTALLATION_NOT_FOUND");
        }
        installationRepository.deleteById(id);
    }

    private void applyPanelSerials(Installation installation, List<String> serials) {
        if (serials == null) {
            return;
        }

        // Diff against existing panels by serial number, rather than clear()+recreate:
        // clearing and re-adding an unchanged serial would make Hibernate try to insert
        // the "new" row before deleting the orphaned old one in the same flush, which
        // trips the unique constraint on panel_serial_number for no reason.
        var existingBySerial = new java.util.HashMap<String, InstallationPanel>();
        for (InstallationPanel p : installation.getPanels()) {
            existingBySerial.put(p.getPanelSerialNumber(), p);
        }

        var keptSerials = new java.util.HashSet<String>();
        int position = 1;
        for (String serial : serials) {
            if (serial == null || serial.isBlank()) {
                position++;
                continue;
            }
            InstallationPanel existing = existingBySerial.get(serial);
            if (existing != null) {
                existing.setPanelPosition(position);
                keptSerials.add(serial);
                position++;
                continue;
            }

            panelRepository.findByPanelSerialNumber(serial).ifPresent(other -> {
                if (installation.getId() == null || !other.getInstallation().getId().equals(installation.getId())) {
                    throw new DuplicateResourceException(
                            "Panel serial " + serial + " is already recorded against another installation");
                }
            });
            installation.getPanels().add(InstallationPanel.builder()
                    .installation(installation)
                    .panelPosition(position)
                    .panelSerialNumber(serial)
                    .build());
            keptSerials.add(serial);
            position++;
        }

        // orphanRemoval=true on the collection means removing an entry here deletes its row
        installation.getPanels().removeIf(p -> !keptSerials.contains(p.getPanelSerialNumber()));
    }

    private void assertNoDuplicateKeys(String ulaApplicationId, String serviceNumber, Long excludeId) {
        boolean ulaTaken = excludeId == null
                ? installationRepository.existsByUlaApplicationId(ulaApplicationId)
                : installationRepository.existsByUlaApplicationIdAndIdNot(ulaApplicationId, excludeId);
        if (ulaTaken) {
            throw new DuplicateResourceException("ULA Application ID " + ulaApplicationId + " already exists");
        }
        boolean serviceTaken = excludeId == null
                ? installationRepository.existsByServiceNumber(serviceNumber)
                : installationRepository.existsByServiceNumberAndIdNot(serviceNumber, excludeId);
        if (serviceTaken) {
            throw new DuplicateResourceException("Service number " + serviceNumber + " already exists");
        }
    }

    private Installation findEntity(Long id) {
        return installationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installation not found: " + id, "INSTALLATION_NOT_FOUND"));
    }

    private Consumer resolveConsumer(Long consumerId) {
        if (consumerId == null) return null;
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found: " + consumerId, "CONSUMER_NOT_FOUND"));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Installer not found: " + userId));
    }
}
