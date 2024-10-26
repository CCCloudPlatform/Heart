package com.cloudclub.userservice.dao;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Data
@Entity
@Table(name = "users")
public class UserDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;
    private String userID;
    private String userDetail;
    private String userRole;
    private String userEmail;
    private String userName;
    private String userPriority;
    private boolean approve;
    private String encryptedPW;
}
