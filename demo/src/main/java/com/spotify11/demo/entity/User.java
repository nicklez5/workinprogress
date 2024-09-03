package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;
    private String password;
    private String email;
    private String role;

    public User(String name, String email, String password,String role) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="LIBRARY_ID")
    private Library library = new Library();


    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="PLAYLIST_ID")
    private List<Playlist> playlist = new ArrayList<>();


    public Integer randomId(){
        Random rand = new Random();
        int n = rand.nextInt(1000);
        n += 1;
        return n;
    }
    public Integer getId(){
        return userId;
    }
    public void setId(Integer id){
        this.userId = id;
    }



    public Playlist getPlaylist(Integer id) {
        return playlist.get(id);
    }

    public void addPlaylist(Playlist playlist) {
        this.playlist.add(playlist);
    }

}
