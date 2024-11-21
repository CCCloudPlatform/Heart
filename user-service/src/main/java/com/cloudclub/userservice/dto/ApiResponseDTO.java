package com.cloudclub.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .status("error")
                .message(message)
                .build();
    }
}
