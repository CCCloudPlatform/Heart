package com.cloudclub.userservice.controller;

import com.cloudclub.userservice.dto.ApiResponseDTO;
import com.cloudclub.userservice.dto.AuthResponseData;
import com.cloudclub.userservice.dto.UserDTO;
import com.cloudclub.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = AuthResponseData.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseData>> login(@RequestBody UserDTO userDTO) {
        try {
            AuthResponseData response = userService.login(userDTO);
            return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (userService.logout(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "회원가입", description = "사용자 회원가입을 처리합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "회원가입 실패")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<AuthResponseData>> register(@RequestBody UserDTO userDTO) {
        try {
            AuthResponseData response = userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Registration successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "토큰 갱신", description = "사용자 토큰을 갱신합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = userService.refreshToken(token);
        if (newToken != null) {
            return ResponseEntity.ok().body(Map.of("Access-Token", newToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "사용자 조회", description = "사용자 ID를 기반으로 사용자를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userID}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserByID(@PathVariable String userID) {
        try {
            UserDTO userDTO = userService.getUserByID(userID);
            return ResponseEntity.ok(ApiResponseDTO.success("User found", userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "사용자 업데이트", description = "사용자 정보를 업데이트합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "사용자 업데이트 실패")
    })
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(ApiResponseDTO.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "사용자 삭제", description = "사용자 ID를 기반으로 사용자를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable String userID) {
        try {
            userService.deleteUser(userID);
            return ResponseEntity.ok(ApiResponseDTO.success("User deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
