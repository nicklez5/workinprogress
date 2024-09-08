package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer user_id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="library_id")
    private Library library;


    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(name = "PLAYLIST_SONG_MAPPING", joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> Playlists = new HashSet<>();

    public Users(String username, String email, String password,String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }




    public Integer randomId(){
        Random rand = new Random();
        int n = rand.nextInt(1000);
        n += 1;
        return n;
    }

    public Playlist getPlaylist(Integer playlist_id) {
        for(Playlist p : Playlists){
            if(p.getId().equals(playlist_id)){
                return p;
            }
        }
        return (Playlist) this.Playlists;
    }

    public void addPlaylist(Playlist playlist) {
        this.Playlists.add(playlist);
    }
    public void removePlaylist(Playlist playlist){
        this.Playlists.remove(playlist);
    }



}
