package com.solartracker.exception;

public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(String message) {
        this(message, "BUSINESS_RULE_VIOLATION");
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
