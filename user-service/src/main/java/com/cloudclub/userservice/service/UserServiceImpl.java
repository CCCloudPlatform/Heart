package com.cloudclub.userservice.service;

import com.cloudclub.userservice.dto.UserDTO;
import com.cloudclub.userservice.dao.UserDAO;
import com.cloudclub.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 간단한 토큰 저장소 (실제로는 Redis 등을 사용하는 것이 좋습니다)
    private Map<String, String> tokenStore = new HashMap<>();

    @Override
    public String login(UserDTO userDTO) {
        Optional<UserDAO> user = userRepository.findByUserID(userDTO.getUserID());
        if (user.isPresent() && passwordEncoder.matches(userDTO.getUserPW(), user.get().getEncryptedPW())) {
            String token = generateToken();
            tokenStore.put(token, userDTO.getUserID());
            return token;
        }
        return null;
    }

    @Override
    public boolean logout(String token) {
        return tokenStore.remove(token) != null;
    }

    @Override
    public boolean register(UserDTO userDTO) {
        if (userRepository.findByUserID(userDTO.getUserID()).isPresent()) {
            return false; // 이미 존재하는 사용자
        }
        UserDAO newUser = new UserDAO();
        newUser.setUserID(userDTO.getUserID());
        newUser.setEncryptedPW(passwordEncoder.encode(userDTO.getUserPW()));
        newUser.setUserName(userDTO.getUserName());
        newUser.setUserEmail(userDTO.getUserEmail());
        newUser.setUserRole(userDTO.getUserRole());
        newUser.setUserDetail(userDTO.getUserDetail());
        userRepository.save(newUser);
        return true;
    }

    @Override
    public String refreshToken(String token) {
        String userID = tokenStore.get(token);
        if (userID != null) {
            tokenStore.remove(token);
            String newToken = generateToken();
            tokenStore.put(newToken, userID);
            return newToken;
        }
        return null;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UserDTO getUserByID(String userID) {
        Optional<UserDAO> userDAO = userRepository.findByUserID(userID);
        if (userDAO.isPresent()) {
            return convertToDTO(userDAO.get());
        }
        return null;
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

    private UserDTO convertToDTO(UserDAO userDAO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(userDAO.getUserID());
        userDTO.setUserName(userDAO.getUserName());
        userDTO.setUserEmail(userDAO.getUserEmail());
        userDTO.setUserRole(userDAO.getUserRole());
        userDTO.setUserDetail(userDAO.getUserDetail());
        // Note: We don't set the password in DTO for security reasons
        return userDTO;
    }

    private UserDAO updateUserDAO(UserDAO existingUser, UserDTO userDTO) {
        existingUser.setUserName(userDTO.getUserName());
        existingUser.setUserEmail(userDTO.getUserEmail());
        existingUser.setUserRole(userDTO.getUserRole());
        existingUser.setUserDetail(userDTO.getUserDetail());
        // Note: Update password only if it's provided in the DTO
        if (userDTO.getUserPW() != null && !userDTO.getUserPW().isEmpty()) {
            // TODO: Implement password encryption
            existingUser.setEncryptedPW(passwordEncoder.encode(userDTO.getUserPW()));
        }
        return existingUser;
    }
}
