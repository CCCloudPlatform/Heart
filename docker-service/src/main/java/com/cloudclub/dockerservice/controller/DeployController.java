package com.cloudclub.dockerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudclub.dockerservice.service.DeployService;

@RestController
@RequiredArgsConstructor
public class DeployController {

    private final DeployService deployService;

    @PostMapping("/docker-service/one-click-deploy")
    public ResponseEntity<String> oneClickDeploy() {
        deployService.deploy();
        return ResponseEntity.ok("Deployment initiated.");
    }
}
