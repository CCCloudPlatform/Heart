package com.cloudclub.userservice.repository;

import com.cloudclub.userservice.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserID(String userID);
    boolean existsByUserID(String userID);
    boolean existsByUserEmail(String userEmail);
}
