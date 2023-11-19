package com.sevilay.exception;

import lombok.Getter;


@Getter
public class AuthServiceException extends RuntimeException {

    private final ErrorType errorType;

    public AuthServiceException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }

    public AuthServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }


}
