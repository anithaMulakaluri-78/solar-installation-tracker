package com.solartracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstallationRequest {

    private Long consumerId;

    @NotBlank(message = "ULA Application ID is required")
    private String ulaApplicationId;

    private String section;
    private String distribution;

    @NotBlank(message = "Service number is required")
    private String serviceNumber;

    @NotBlank(message = "Consumer name is required")
    private String consumerName;

    private String consumerMobile;
    private String newMobile;
    private String inverterSerialNumber;
    private String siteType;
    private String remarks;
    private Long assignedInstallerId;

    // Panel serial numbers, in order (position = index + 1). Empty/null is fine - panels
    // are often added later during the actual installer scan flow, not at record creation.
    private List<String> panelSerialNumbers;
}
