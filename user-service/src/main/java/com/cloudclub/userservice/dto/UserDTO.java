package com.cloudclub.userservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userID;
    private String userPW;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userDetail;

    // 기본 생성자 추가
    public UserDTO() {}
}
