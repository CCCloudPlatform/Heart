package com.cloudclub.userservice.service;

import com.cloudclub.userservice.dto.UserDTO;

public interface UserService {
    String login(UserDTO userDTO);
    boolean logout(String token);
    boolean register(UserDTO userDTO);
    String refreshToken(String token);
    UserDTO getUserByID(String userID);
    boolean updateUser(UserDTO userDTO);
    boolean deleteUser(String userID);
}
