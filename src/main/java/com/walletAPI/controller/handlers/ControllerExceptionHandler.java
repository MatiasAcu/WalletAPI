package com.walletAPI.controller.handlers;

import com.walletAPI.controller.responses.ErrorResponse;
import com.walletAPI.controller.responses.ErrorResponseBuilder;
import com.walletAPI.model.service.exceptions.AccountException;
import com.walletAPI.model.service.exceptions.TransactionException;
import com.walletAPI.model.service.exceptions.UserExceptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponseBuilder errorResponseBuilder;


    public ControllerExceptionHandler(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }

    /*
     * Handles CustomUserExceptions and returns a response with a list of all exceptions
     * */
    @ExceptionHandler(UserExceptions.class)
    public ResponseEntity<Object> handleCustomUserException(
            UserExceptions exceptions
    ) {
        Map<String, List<ErrorResponse>> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        if (!exceptions.getExceptions().isEmpty()) {
            errorResponseList =
                    exceptions.getExceptions().stream()
                            .map(errorResponseBuilder::build)
                            .collect(Collectors.toList());
        } else {
            errorResponseList.add(errorResponseBuilder.build(exceptions));
        }
        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /*
     * Handles CustomAccountExceptions and returns a response with a list of all exceptions
     * */
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Object> handleCustomAccountException(AccountException exception) {
        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(errorResponseBuilder.build(exception));

        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /*
     * Handles TransactionExceptions and returns a response with a list of all exceptions
     * */
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<Object> handleTransactionException(TransactionException exception) {

        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(errorResponseBuilder.build(exception));

        body.put("errors", errorResponseList);

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /*
     * Handles validation exceptions from the http requests
     * and returns a response with a list of all exceptions
     * */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        List<ErrorResponse> errors = ex.getAllErrors()
                .stream()
                .map(errorResponseBuilder::build)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);

    }


}




