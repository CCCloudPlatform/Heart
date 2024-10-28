package com.cloudclub.openstackservice.common.exception.errorcode;

import com.cloudclub.openstackservice.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VmErrorCode implements ErrorCode {
    EMPTY_VM("VM_001", "해당 VM이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    VM_POWER_ON_FAILED("VM_002", "VM을 켜는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    VM_POWER_OFF_FAILED("VM_003", "VM을 끄는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    VM_REBOOT_FAILED("VM_004", "VM을 재부팅하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    VM_CREATE_FAILED("VM_005", "VM을 생성하는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
