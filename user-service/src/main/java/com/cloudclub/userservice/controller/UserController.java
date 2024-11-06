package com.cloudclub.userservice.controller;

import com.cloudclub.userservice.dto.ApiResponse;
import com.cloudclub.userservice.dto.AuthResponseData;
import com.cloudclub.userservice.dto.UserDTO;
import com.cloudclub.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseData>> login(@RequestBody UserDTO userDTO) {
        try {
            AuthResponseData response = userService.login(userDTO);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (userService.logout(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseData>> register(@RequestBody UserDTO userDTO) {
        try {
            AuthResponseData response = userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = userService.refreshToken(token);
        if (newToken != null) {
            return ResponseEntity.ok().body(Map.of("Access-Token", newToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{userID}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByID(@PathVariable String userID) {
        try {
            UserDTO userDTO = userService.getUserByID(userID);
            return ResponseEntity.ok(ApiResponse.success("User found", userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userID) {
        try {
            userService.deleteUser(userID);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
