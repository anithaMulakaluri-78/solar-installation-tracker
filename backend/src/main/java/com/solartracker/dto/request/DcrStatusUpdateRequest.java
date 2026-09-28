package com.solartracker.dto.request;

import com.solartracker.entity.enums.DcrStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcrStatusUpdateRequest {

    @NotNull(message = "DCR status is required")
    private DcrStatus dcrStatus;

    // Optional note explaining the status change (e.g. rejection reason)
    private String remarks;
}
