package com.hotelbooking.airbnb.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException {
    private static final long serialVersionUId = 1L;
    private final HttpStatus httpStatus;

    public APIException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST; // Default status
    }

    public APIException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}