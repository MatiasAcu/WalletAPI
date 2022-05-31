package com.walletAPI.controller.responses;

import java.time.Instant;


public class ErrorResponse {

    private static final String ERROR_TAG = "ERROR.";

    private final Instant timestamp;
    private String errorCode;
    private String message;

    public ErrorResponse() {
        this.timestamp = Instant.now();
    }


    public ErrorResponse(String errorCode, String message) {
        this.errorCode = ERROR_TAG + errorCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = ERROR_TAG + errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"errorCode\": \"" + errorCode + '\"' +
                ", \"message\": \"" + message + '\"' +
                ", \"timestamp\": " + timestamp +
                '}';
    }
}
