package com.r2s.mobile_store.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private List<String> errors;
    private String timestamp;
    private String path;

    public ApiResponse() {
        this.timestamp = Instant.now().toString();
    }

    public ApiResponse(String status, String message, T data, List<String> errors, String path) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = Instant.now().toString();
        this.path = path;
    }


}
