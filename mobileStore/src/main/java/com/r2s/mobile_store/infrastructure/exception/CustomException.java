package com.r2s.mobile_store.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class CustomException extends RuntimeException {
    private final List<Error> errors;

    public CustomException(List<Error> errors) {
        super("Multiple validation errors occurred");
        this.errors = errors;
    }

    public CustomException(Error error) {
        super(error.getMessage());
        this.errors = List.of(error);
    }
    public HttpStatus getStatusCode() {
        // Return the HttpStatus converted from HttpStatusCode of the first error, or a default status
        return errors != null && !errors.isEmpty()
                ? HttpStatus.valueOf(errors.get(0).getStatusCode().value()) // Convert HttpStatusCode to HttpStatus
                : HttpStatus.BAD_REQUEST;
    }

}