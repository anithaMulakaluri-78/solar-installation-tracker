package com.solartracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String message;
    private String errorCode;
    private Map<String, String> errors;
}
