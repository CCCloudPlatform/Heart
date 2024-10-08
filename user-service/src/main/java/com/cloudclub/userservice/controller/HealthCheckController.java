package com.cloudclub.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("/health_check")
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK).body(String.format("It's Working in User Service"));
    }
}
