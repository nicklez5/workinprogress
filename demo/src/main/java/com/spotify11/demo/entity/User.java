package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.spotify11.demo.entity.Library;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    private String name;
    private String password;
    private String email;
    private String role;

    public User(Integer userId, String name, String email, String password,String role) {
        super();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="library_id",referencedColumnName = "LIBRARY_ID")
    })
    private Library library;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="playlist_id", referencedColumnName="PLAYLIST_ID"),
    })
    private Playlist playlist;

    public Integer getId(){
        return userId;
    }
    public void setId(Integer id){
        this.userId = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", library=" + library +
                ", playlist=" + playlist +
                '}';
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Library getLibrary() {
        return this.library;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

}
