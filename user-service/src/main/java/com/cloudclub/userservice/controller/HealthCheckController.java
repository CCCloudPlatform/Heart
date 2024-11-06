package com.cloudclub.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("/health_check")
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK).body(String.format("It's Working in User Service"));
    }

    @GetMapping("/headerCheck")
    public ResponseEntity<?> getCloudClub(HttpServletRequest request) {
        // 모든 헤더 출력
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        // 특정 헤더 조회
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Role");

        System.out.println("Authorization: " + authorization);
        System.out.println("X-User-Id: " + userId);
        System.out.println("X-User-Role: " + userRole);

        return ResponseEntity.ok().build();
    }
}
