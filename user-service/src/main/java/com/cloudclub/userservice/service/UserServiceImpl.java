package com.cloudclub.userservice.service;

import com.cloudclub.userservice.dto.UserDTO;
import com.cloudclub.userservice.dao.UserEntity;
import com.cloudclub.userservice.repository.UserRepository;
import com.cloudclub.userservice.security.JwtUtil;
import com.cloudclub.userservice.dto.AuthResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseData login(UserDTO userDTO) {
        UserEntity user = userRepository.findByUserID(userDTO.getUserID())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        if (passwordEncoder.matches(userDTO.getUserPW(), user.getEncryptedPW())) {
            String token = jwtUtil.createToken(user.getUserID(), user.getUserRole());
            return AuthResponseData.builder()
                .accessToken(token)
                .user(convertToDTO(user))
                .build();
        }
        throw new RuntimeException("Invalid password");
    }

    @Override
    public AuthResponseData register(UserDTO userDTO) {
        if (userRepository.findByUserID(userDTO.getUserID()).isPresent()) {
            throw new RuntimeException("UserID already exists");
        }

        UserEntity user = UserEntity.builder()
                .userID(userDTO.getUserID())
                .encryptedPW(passwordEncoder.encode(userDTO.getUserPW()))
                .userName(userDTO.getUserName())
                .userEmail(userDTO.getUserEmail())
                .userRole(userDTO.getUserRole())
                .userDetail(userDTO.getUserDetail())
                .build();
            
        userRepository.save(user);
        
        String token = jwtUtil.createToken(user.getUserID(), user.getUserRole());
        return AuthResponseData.builder()
            .accessToken(token)
            .user(convertToDTO(user))
            .build();
    }

    @Override
    public boolean logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.validateToken(token);
        }
        return false;
    }

    @Override
    public String refreshToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                String userID = jwtUtil.getUserIdFromToken(token);
                String userRole = jwtUtil.getRoleFromToken(token);
                return jwtUtil.createToken(userID, userRole);
            }
        }
        return null;
    }

    @Override
    public UserDTO getUserByID(String userID) {
        UserEntity user = userRepository.findByUserID(userID)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        Optional<UserDAO> existingUser = userRepository.findByUserID(userDTO.getUserID());
        if (existingUser.isPresent()) {
            UserDAO updatedUser = updateUserDAO(existingUser.get(), userDTO);
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(String userID) {
        Optional<UserDAO> user = userRepository.findByUserID(userID);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    private UserDTO convertToDTO(UserEntity user) {
        return UserDTO.builder()
            .userID(user.getUserID())
            .userName(user.getUserName())
            .userEmail(user.getUserEmail())
            .userRole(user.getUserRole())
            .userDetail(user.getUserDetail())
            .build();
    }

    private UserDAO updateUserDAO(UserDAO existingUser, UserDTO userDTO) {
        existingUser.setUserName(userDTO.getUserName());
        existingUser.setUserEmail(userDTO.getUserEmail());
        existingUser.setUserRole(userDTO.getUserRole());
        existingUser.setUserDetail(userDTO.getUserDetail());
        // Note: Update password only if it's provided in the DTO
        if (userDTO.getUserPW() != null && !userDTO.getUserPW().isEmpty()) {
            user.setEncryptedPW(passwordEncoder.encode(userDTO.getUserPW()));
        }
    }
}
