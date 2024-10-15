package com.r2s.mobile_store.infrastructure.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum Error {
    //Client Error
    NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND), //Resource not found
    BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST), //Syntax error or malformed request
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED), // unauthenticated user
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN), //The user does not have permission to access the resource
    CONFLICT(409, "Conflict", HttpStatus.CONFLICT), // Resource state conflicts. For example, it can happen when trying to create a duplicate record or update data that is being edited at the same time by someone else.
    //Server Error
    UNCATEGORIZED_EXCEPTION(9999, "Unclassified error", HttpStatus.INTERNAL_SERVER_ERROR),
    //Database Error
    DATABASE_ACCESS_ERROR(9998, "Database access error", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_KEY(9996, "Duplicate key found", HttpStatus.CONFLICT),
    EMPTY_RESULT(9995, "No result found", HttpStatus.NOT_FOUND),
    NON_UNIQUE_RESULT(9994, "Non-unique result found", HttpStatus.CONFLICT),
    //User-related errors
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.CONFLICT),
    USER_UNABLE_TO_SAVE(1003, "Unable to save user", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_UNABLE_TO_UPDATE(1004, "Unable to update user", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_UNABLE_TO_DELETE(1005, "Unable to delete user", HttpStatus.INTERNAL_SERVER_ERROR),
    //Product error codes
    PRODUCT_NOT_FOUND(2001, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2002, "Product already exists", HttpStatus.CONFLICT),
    PRODUCT_UNABLE_TO_SAVE(2003, "Unable to save product", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_UNABLE_TO_UPDATE(2004, "Unable to update product", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_UNABLE_TO_DELETE(2005, "Unable to delete product", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_INVALID_NAME(2006, "Invalid name", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_PRICE(2007, "Invalid price", HttpStatus.BAD_REQUEST),
    //Order error codes
    //Category error codes
    CATEGORY_NOT_FOUND(3001, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(3002, "Category already exists", HttpStatus.CONFLICT),
    CATEGORY_UNABLE_TO_SAVE(3003, "Unable to save category", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_UNABLE_TO_UPDATE(3004, "Unable to update category", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_UNABLE_TO_DELETE(3005, "Unable to delete category", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_INVALID_NAME(3006, "Invalid name", HttpStatus.BAD_REQUEST),
    //Jwt token-related error
    JWT_INVALID(1101, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
    JWT_EXPIRED(1102, "JWT token expired", HttpStatus.UNAUTHORIZED),
    JWT_MALFORMED(1103, "Malformed JWT token", HttpStatus.UNAUTHORIZED),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    /**
     * Constructor for ErrorCode.
     *
     * @param code       the error code
     * @param message    the error message
     * @param statusCode the corresponding HTTP status code
     */
    Error(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
