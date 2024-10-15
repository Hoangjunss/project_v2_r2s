package com.r2s.mobile_store.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomJwtException extends RuntimeException {
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
    private final Error error;  // Thêm trường Error

    public CustomJwtException(Error error, Throwable cause) {
        super(error.getMessage(), cause);
        this.code = error.getCode();
        this.message = error.getMessage();
        this.statusCode = error.getStatusCode();
        this.error = error;
    }

    public CustomJwtException(Error error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.message = error.getMessage();
        this.statusCode = error.getStatusCode();
        this.error = error;  // Lưu lại đối tượng Error
    }

    // Thêm phương thức getError để lấy Error
    public Error getError() {
        return this.error;
    }
}