package com.cloudclub.userservice.service;

import com.cloudclub.userservice.dto.AuthResponseData;
import com.cloudclub.userservice.dto.UserDTO;

public interface UserService {
    AuthResponseData login(UserDTO userDTO);
    AuthResponseData register(UserDTO userDTO);
    boolean logout(String token);
    String refreshToken(String token);
    UserDTO getUserByID(String userID);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(String userID);
}
