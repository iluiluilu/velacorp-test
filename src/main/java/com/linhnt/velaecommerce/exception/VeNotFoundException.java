package com.linhnt.velaecommerce.exception;


import com.linhnt.velaecommerce.constant.ErrorCode;
import lombok.Getter;

@Getter
public class VeNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public VeNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
