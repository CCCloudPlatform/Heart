package com.cloudclub.openstackservice.common.exception;

import com.cloudclub.openstackservice.common.exception.errorcode.GlobalErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    /**
     * CASE: 미리 지정해놓은 에러
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getErrorCode(), e.getMessage()), e.getStatus());
    }

    /**
     * CASE: 서버 내부 에러
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        return convert(GlobalErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> convert(ErrorCode e, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ExceptionResponse(e.getErrorCode(), e.getMessage()), httpStatus);
    }
}
