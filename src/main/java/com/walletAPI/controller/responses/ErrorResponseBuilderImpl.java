package com.walletAPI.controller.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.Optional;

@Service
public class ErrorResponseBuilderImpl implements ErrorResponseBuilder {

    private final ObjectMapper objectMapper = new JsonMapper();


    public ErrorResponseBuilderImpl() {
    }

    @Override
    public ErrorResponse build(RuntimeException e) {

        Optional<ErrorResponse> body = Optional.ofNullable(parseErrorResponse(e.getMessage()));
        return body.orElseGet(() -> new ErrorResponse("UNEXPECTED_EXCEPTION", e.getMessage()));
    }

    public ErrorResponse build(ObjectError e) {

        Optional<ErrorResponse> body = Optional.ofNullable(parseErrorResponse(e.getDefaultMessage()));
        return body.orElseGet(() -> new ErrorResponse("UNEXPECTED_ERROR", e.getDefaultMessage()));
    }

    @Override
    public ErrorResponse build(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

    private ErrorResponse parseErrorResponse(String json) {
        try {
            return objectMapper.readValue(json, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
