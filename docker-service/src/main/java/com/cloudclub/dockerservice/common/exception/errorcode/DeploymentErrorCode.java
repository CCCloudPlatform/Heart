package com.cloudclub.dockerservice.common.exception.errorcode;

import com.cloudclub.dockerservice.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeploymentErrorCode implements ErrorCode {
    REPOSITORY_CLONE_FAILED("DEPLOYMENT_001", "Repository Clone에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    DOCKER_IMAGE_BUILD_FAILED("DEPLOYMENT_002", "Docker 이미지 빌드에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    DOCKER_IMAGE_PUSH_FAILED("DEPLOYMENT_003", "Docker 이미지 푸쉬에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
