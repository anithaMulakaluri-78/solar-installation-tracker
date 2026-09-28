package com.solartracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String username;
    private String fullName;
}
