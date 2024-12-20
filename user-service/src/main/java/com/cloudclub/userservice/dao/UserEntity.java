package com.cloudclub.userservice.dao;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;
    private String userID;
    private String userDetail;
    private String userRole;
    private String userEmail;
    private String userName;
    private String encryptedPW;

    public UserEntity() {}
}
