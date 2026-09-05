package com.retailer.rewards.exception;

import com.retailer.rewards.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Not Found", Instant.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorResponse body = new ErrorResponse(msg, HttpStatus.BAD_REQUEST.value(), "Bad Request", Instant.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            Object statusObj = null;
            try {
                statusObj = ex.getClass().getMethod("getStatus").invoke(ex);
            } catch (NoSuchMethodException e1) {
                try {
                    statusObj = ex.getClass().getMethod("getStatusCode").invoke(ex);
                } catch (NoSuchMethodException e2) {
                    try {
                        statusObj = ex.getClass().getMethod("getRawStatusCode").invoke(ex);
                    } catch (NoSuchMethodException e3) {
                        statusObj = null;
                    }
                }
            }

            if (statusObj instanceof HttpStatus) {
                status = (HttpStatus) statusObj;
            } else if (statusObj instanceof Integer) {
                status = HttpStatus.valueOf((Integer) statusObj);
            }
        } catch (Exception ignore) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String reason = null;
        try {
            Object r = ex.getClass().getMethod("getReason").invoke(ex);
            if (r != null) reason = r.toString();
        } catch (Exception ignore) {
        }

        ErrorResponse body = new ErrorResponse(reason != null ? reason : ex.getMessage(), status.value(), status.getReasonPhrase(), Instant.now());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        ErrorResponse body = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", Instant.now());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
