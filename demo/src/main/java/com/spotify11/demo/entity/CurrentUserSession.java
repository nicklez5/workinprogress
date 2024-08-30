package com.spotify11.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CurrentUserSession {
    
    @Id
    private String email;
    private Integer userId;
    private String uuId;

    public CurrentUserSession(){
        
    }

    public CurrentUserSession(String email, Integer userId, String uuId) {
        super();
        this.email = email;
        this.userId = userId;
        this.uuId = uuId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    @Override
    public String toString() {
        return "CurrentUserSession [email=" + email + ", userId=" + userId + ", uuId=" + uuId + "]";
    }
}
