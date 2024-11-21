package com.cloudclub.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseData {
    private String accessToken;
    private UserDTO user;
}
