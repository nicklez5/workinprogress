package com.spotify11.demo.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
public class Login {
    
    private String email;
    private String password;
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login [email=" + email + ", password=" + password + "]";
    }
}
