package com.sevilay.exception;

import lombok.Getter;


@Getter
public class UserServiceException extends RuntimeException {

    private final ErrorType errorType;

    public UserServiceException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }

    public UserServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }


}
