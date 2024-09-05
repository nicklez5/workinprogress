package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Playlist(String playlist_name, Integer playlist_id) {
        this.id = playlist_id;
        this.name = playlist_name;
    }
    public Playlist(String playlist_name) {
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "playlist_songs", referencedColumnName = "id")
    private List<Song> songs = new ArrayList<>();


    public void addSongs(Song song) {
        this.songs.add(song);
    }
    public void deleteSongs(Song song) {
        this.songs.remove(song);
    }
    public void removeAllSongs(){
        this.songs.clear();

    }
    public Integer randomId(){
        Random rand = new Random();
        int n = rand.nextInt(100000);
        n += 1;
        return n;
    }


}
