package com.hotelbooking.airbnb.advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<FieldError> subErrors;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
