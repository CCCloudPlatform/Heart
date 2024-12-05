package com.cloudclub.dockerservice.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();
    String getMessage();
    HttpStatus getStatus();
}
