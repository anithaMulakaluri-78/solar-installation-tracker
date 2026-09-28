package com.solartracker.dto.response;

import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class InstallationResponse {
    private Long id;
    private Long consumerId;
    private String consumerNumber;
    private String ulaApplicationId;
    private String section;
    private String distribution;
    private String serviceNumber;
    private String consumerName;
    private String consumerMobile;
    private String newMobile;
    private String inverterSerialNumber;
    private String siteType;
    private String remarks;
    private DcrStatus dcrStatus;
    private InstallationStatus status;
    private UserSummaryResponse assignedInstaller;
    private List<String> panelSerialNumbers;
    private LocalDateTime createdAt;
}
