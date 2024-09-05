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
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    public User(String name, String email, String password,String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="user_library")
    private Library library = new Library();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_playlists", referencedColumnName = "id")
    private List<Playlist> Playlists = new ArrayList<>();


    public Integer randomId(){
        Random rand = new Random();
        int n = rand.nextInt(1000);
        n += 1;
        return n;
    }

    public Playlist getPlaylist(Integer id) {
        for(Playlist p : Playlists){
            if(p.getId().equals(id)){
                return p;
            }
        }
        return this.Playlists.get(id);
    }

    public void addPlaylist(Playlist playlist) {
        this.Playlists.add(playlist);
    }
    public void removePlaylist(Playlist playlist){
        this.Playlists.remove(playlist);
    }
    public void setPlaylist(Playlist playlist){
        this.Playlists.set(playlist.getId(), playlist);
    }


}
