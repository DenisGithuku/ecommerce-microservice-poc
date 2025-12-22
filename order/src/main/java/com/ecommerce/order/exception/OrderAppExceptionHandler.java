package com.ecommerce.order.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class OrderAppExceptionHandler {

    // handle missing object exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception, HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis()).path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedUser(
            UnauthorizedException exception, HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(System.currentTimeMillis()).path(request.getRequestURI()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .timestamp(System.currentTimeMillis())
                        .path(request.getRequestURI())
                        // optionally add: .errors(errors)
                        .build()
        );
    }


    @ExceptionHandler(DuplicateOrderException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateProductName(DuplicateOrderException exception, HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateCartException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCart(DuplicateCartException exception, HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(
            HttpClientErrorException exception, HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(ErrorResponse.builder()
                        .status(exception.getStatusCode().value())
                        .message("Downstream service error")
                        .timestamp(System.currentTimeMillis())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailabilityException(ServiceUnavailableException exception, HttpServletRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // handle generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception exception, HttpServletRequest request) {
        log.error("Unexpected exception 💥: ", exception);
        var errorResponse = ErrorResponse.builder()
                .message("Internal server error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}