package com.ota.notetaking.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        HttpStatus status =  HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = exception.getMessage();
        String error = exception.getClass().getSimpleName();

        try {
            if (exception instanceof WebExchangeBindException webExchangeBindException) {
                status = HttpStatus.valueOf(webExchangeBindException.getStatusCode().value());
                errorMessage = webExchangeBindException.getFieldErrors().parallelStream()
                        .map(FieldError::getDefaultMessage)
                        .toList().toString();
            } else {
                status = HttpStatus.valueOf(Integer.parseInt(exception.getMessage().substring(0, 3)));
                errorMessage = exception.getMessage().replaceAll(".*\"([^\"]*)\".*", "$1");
            }
        } catch (Exception e) {
            log.info("Unexpected error occurred: " + exception.getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(status.value(), error, errorMessage, LocalDateTime.now());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;
    }
}
