package com.cloudclub.dockerservice.controller;

import com.cloudclub.dockerservice.dto.DeploymentRequestDto;
import com.cloudclub.dockerservice.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docker-service")
public class DeploymentController {

    private final DeploymentService deploymentService;

    @PostMapping("/one-click-deploy")
    public String oneClickDeploy(@RequestBody DeploymentRequestDto requestDto) {
        deploymentService.deploy(requestDto);
        return "Deployment completed successfully";
    }
}
