package com.cloudclub.openstackservice.common.exception.errorcode;

import com.cloudclub.openstackservice.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    SERVER_ERROR("GLOBAL_001", "서버와의 연결에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
