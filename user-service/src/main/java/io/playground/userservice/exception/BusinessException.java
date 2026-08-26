package io.playground.userservice.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessErrorCode errorCode;
    private final String details;

    public BusinessException(BusinessErrorCode errorCode) {
        this.errorCode = errorCode;
        this.details = null;
    }

    public BusinessException(BusinessErrorCode errorCode,
                             String details) {
        this.errorCode = errorCode;
        this.details = details;
    }
}
