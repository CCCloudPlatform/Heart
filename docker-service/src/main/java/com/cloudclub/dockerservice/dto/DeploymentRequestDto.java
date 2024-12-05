package com.cloudclub.dockerservice.dto;

import com.cloudclub.dockerservice.DeploymentProjectType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeploymentRequestDto {

    private String githubRepoUrl;

    private DeploymentProjectType deploymentProjectType;
}
