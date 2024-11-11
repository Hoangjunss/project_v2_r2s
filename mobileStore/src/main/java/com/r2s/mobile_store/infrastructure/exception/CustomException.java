package com.r2s.mobile_store.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomException extends RuntimeException {
    private final List<Error> errors;
    private final String additionalDetail; // Lưu trữ ID hoặc thông tin bổ sung cho thông báo lỗi

    // Constructor sử dụng danh sách lỗi và không có additionalDetail
    public CustomException(List<Error> errors) {
        super(errors.stream().map(Error::getMessage).collect(Collectors.joining(", ")));
        this.errors = errors;
        this.additionalDetail = null; // Đặt null vì không có additionalDetail
    }

    // Constructor sử dụng một lỗi duy nhất và không có additionalDetail
    public CustomException(Error error) {
        super(error.getMessage());
        this.errors = List.of(error);
        this.additionalDetail = null; // Đặt null vì không có additionalDetail
    }

    // Constructor với danh sách lỗi và thêm thông tin chi tiết (additionalDetail)
    public CustomException(List<Error> errors, String additionalDetail) {
        super(errors.stream()
                .map(e -> e.getMessage() + (additionalDetail != null ? " (ID: " + additionalDetail + ")" : ""))
                .collect(Collectors.joining(", ")));
        this.errors = errors;
        this.additionalDetail = additionalDetail; // Lưu additionalDetail để sử dụng khi cần
    }

    // Phương thức gốc không thay đổi, trả về danh sách thông báo lỗi không có ID
    public List<String> getErrorMessages() {
        return errors.stream().map(Error::getMessage).collect(Collectors.toList());
    }

    // Phương thức mới trả về danh sách thông báo lỗi có thêm ID nếu có additionalDetail
    public List<String> getErrorMessagesWithId() {
        return errors.stream()
                .map(e -> e.getMessage() + (additionalDetail != null ? " (ID: " + additionalDetail + ")" : ""))
                .collect(Collectors.toList());
    }

    // Trả về mã trạng thái HttpStatus của lỗi đầu tiên trong danh sách hoặc mặc định là BAD_REQUEST
    public HttpStatus getStatusCode() {
        return errors != null && !errors.isEmpty()
                ? HttpStatus.valueOf(errors.get(0).getStatusCode().value())
                : HttpStatus.BAD_REQUEST;
    }
}
