package com.invex.employee.mx.exceptions;

public abstract class ApiException extends RuntimeException {
    private final String errorCode;

    protected ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ApiException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
