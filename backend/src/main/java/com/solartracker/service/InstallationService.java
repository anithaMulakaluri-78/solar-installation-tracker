package com.solartracker.service;

import com.solartracker.dto.request.DcrStatusUpdateRequest;
import com.solartracker.dto.request.InstallationRequest;
import com.solartracker.dto.response.InstallationResponse;
import com.solartracker.dto.response.PageResponse;
import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import org.springframework.data.domain.Pageable;

public interface InstallationService {
    PageResponse<InstallationResponse> list(String search, DcrStatus dcrStatus, InstallationStatus status, String section, Pageable pageable);
    InstallationResponse getById(Long id);
    InstallationResponse create(InstallationRequest request);
    InstallationResponse update(Long id, InstallationRequest request);
    InstallationResponse updateDcrStatus(Long id, DcrStatusUpdateRequest request);
    void delete(Long id);
}
