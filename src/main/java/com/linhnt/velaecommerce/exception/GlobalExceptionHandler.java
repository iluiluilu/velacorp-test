package com.linhnt.velaecommerce.exception;

import com.linhnt.velaecommerce.config.VeRequestContext;
import com.linhnt.velaecommerce.constant.Constant;
import com.linhnt.velaecommerce.constant.ErrorCode;
import com.linhnt.velaecommerce.dto.reponse.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import static com.linhnt.velaecommerce.VelaEcommerceApplication.appName;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(VeNotFoundException.class)
    public ResponseEntity<CommonResponseDto> handleNotFoundException(VeNotFoundException ex) {
        log.error(Constant.LOG_FORMAT, VeRequestContext.getDeviceId(), VeRequestContext.getRequestId(), appName, ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        CommonResponseDto.builder()
                                .errorCode(ex.getErrorCode().getCode())
                                .errorMessage(ex.getErrorCode().getMessage())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto> handleException(Exception ex) {
        log.error(Constant.LOG_FORMAT, VeRequestContext.getDeviceId(), VeRequestContext.getRequestId(), appName, ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        CommonResponseDto.builder()
                                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                                .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(Constant.LOG_FORMAT, VeRequestContext.getDeviceId(), VeRequestContext.getRequestId(), appName, ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        CommonResponseDto.builder()
                                .data(errors)
                                .errorCode(ErrorCode.BAD_REQUEST.getCode())
                                .errorMessage(ErrorCode.BAD_REQUEST.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(VeValidateException.class)
    public ResponseEntity<CommonResponseDto> handleValidationException(VeValidateException ex) {
        log.error(Constant.LOG_FORMAT, VeRequestContext.getDeviceId(), VeRequestContext.getRequestId(), appName, ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        CommonResponseDto.builder()
                                .data(ex.getData())
                                .errorCode(ex.getErrorCode().getCode())
                                .errorMessage(ex.getErrorCode().getMessage())
                                .build()
                );
    }
}
