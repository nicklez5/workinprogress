package com.spotify11.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Entity
public class CurrentUserSession {
    
    @Id
    private String email;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "uuId")
    private String uuId;

    public CurrentUserSession(){
        
    }

    public CurrentUserSession(String email, Integer userId, String uuId) {
        super();
        this.email = email;
        this.userId = userId;
        this.uuId = uuId;
    }

    @Override
    public String toString() {
        return "CurrentUserSession [email=" + email + ", userId=" + userId + ", uuId=" + uuId + "]";
    }
}
