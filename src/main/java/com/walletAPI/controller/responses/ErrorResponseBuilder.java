package com.walletAPI.controller.responses;

import org.springframework.validation.ObjectError;

public interface ErrorResponseBuilder {
    /* This methods are used to build the HTTP responses in case of Exceptions */

    public ErrorResponse build(RuntimeException e);

    public ErrorResponse build(ObjectError e);

    public ErrorResponse build(String errorCode, String message);

}
