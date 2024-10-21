package com.cloudclub.openstackservice.common.exception.errorcode;

import com.cloudclub.openstackservice.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VmErrorCode implements ErrorCode {
    SERVER_ERROR("VM_001", "", HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
