package com.mcnz.cartel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class UserAccount {

    private String username;
    private String passwordHash;
    private String roles;
    private String refreshTokenHash;

    public UserAccount() {
    }

    public UserAccount(String username, String passwordHash, String roles, String refreshTokenHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.refreshTokenHash = refreshTokenHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRoles() {
        return roles;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }
}
