package com.hotelbooking.airbnb.advice;

import com.hotelbooking.airbnb.exception.APIException;
import com.hotelbooking.airbnb.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException exception){
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleInternalServerError(Exception exception){
        log.error("Internal Server Error: ", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ApiError.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleAPIException(APIException exception) {
        ApiError apiError = ApiError.builder()
                .status(exception.getHttpStatus())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(new ApiResponse<>(apiError), exception.getHttpStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException exception) {

        log.warn("Authentication failed: {}", exception.getMessage());

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwt(JwtException exception) {

        log.warn("JWT expired: {}", exception.getMessage());

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex){
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied", null);
    }

    private ResponseEntity<@NonNull ApiResponse<?>> buildErrorResponse(HttpStatus status, String message, List<ApiError.FieldError> subErrors) {
        ApiError apiError = ApiError.builder()
                .status(status)
                .message(message)
                .subErrors(subErrors)
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), status);
    }
}
