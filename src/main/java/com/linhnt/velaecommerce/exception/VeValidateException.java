package com.linhnt.velaecommerce.exception;


import com.linhnt.velaecommerce.constant.ErrorCode;
import lombok.Getter;

@Getter
public class VeValidateException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object data;

    public VeValidateException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}
