package com.cloudclub.userservice.service;

import com.cloudclub.userservice.dto.AuthResponseData;
import com.cloudclub.userservice.dto.UserDTO;

public interface UserService {
    AuthResponseData login(UserDTO userDTO);
    AuthResponseData register(UserDTO userDTO);
    boolean logout(String token);
    String refreshToken(String token);
    UserDTO getUserByID(String userID);
    boolean updateUser(UserDTO userDTO);
    boolean deleteUser(String userID);
}
