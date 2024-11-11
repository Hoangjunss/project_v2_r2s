package com.r2s.mobile_store.infrastructure.exception;



import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler  {

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                "fail",
                ex.getMessage(),
                null,
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<Object> response = new ApiResponse<>(
                "fail",
                "Validation failed",
                null,
                errors,
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException cx, WebRequest request) {
        // Chọn danh sách lỗi phù hợp: có ID hay không dựa trên additionalDetail
        List<String> errorMessages = (cx.getAdditionalDetail() != null)
                ? cx.getErrorMessagesWithId()
                : cx.getErrorMessages();

        ApiResponse<Object> response = new ApiResponse<>(
                "fail",
                cx.getMessage(),
                null,
                errorMessages,
                request.getDescription(false)
        );

        // Trả về ResponseEntity với mã trạng thái từ CustomException
        return new ResponseEntity<>(response, cx.getStatusCode());
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Object>> handleAllException(Exception e, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                "An unexpected error occurred",
                null,
                Collections.singletonList(e.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                "Authentication failed",
                null,
                Collections.singletonList("Unauthorized access"),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                "Access denied",
                null,
                Collections.singletonList("You do not have permission to access this resource"),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
