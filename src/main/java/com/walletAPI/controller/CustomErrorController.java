package com.walletAPI.controller;

import com.walletAPI.controller.responses.ErrorResponse;
import com.walletAPI.controller.responses.ErrorResponseBuilder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    private final ErrorResponseBuilder errorResponseBuilder;

    public CustomErrorController(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }

    @RequestMapping(PATH)
    public ResponseEntity<Object> handleError(final HttpServletRequest request,
                                              final HttpServletResponse response) {

        final Map<String, Object> body = new HashMap<>();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();


        switch (response.getStatus()) {
            case (HttpServletResponse.SC_NOT_FOUND): {
                errorResponseList.add(errorResponseBuilder.build("NOT_FOUND", "Resource not found"));
                body.put("errors", errorResponseList);
                break;
            }
            case (HttpServletResponse.SC_FORBIDDEN): {
                errorResponseList.add(errorResponseBuilder.build("FORBIDDEN", "Authentication required to access this resource"));
                body.put("errors", errorResponseList);
                break;
            }
            case (HttpServletResponse.SC_INTERNAL_SERVER_ERROR): {
                Optional<Throwable> throwable =
                        Optional.ofNullable((Throwable) request.getAttribute("javax.servlet.error.exception"));

                throwable.ifPresent(value ->
                        {
                            errorResponseList.add(errorResponseBuilder.build("INTERNAL_SERVER_ERROR", value.getMessage()));
                            body.put("errors", errorResponseList);
                        }
                );
                break;
            }

        }

        return ResponseEntity.status(response.getStatus()).body(body);

    }

    public String getErrorPath() {
        return PATH;
    }

}
