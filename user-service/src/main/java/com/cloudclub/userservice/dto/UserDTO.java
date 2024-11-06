package com.cloudclub.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String userID;
    private String userPW;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userDetail;
}
