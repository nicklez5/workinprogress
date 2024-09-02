package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
@Table(name="playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYLIST_ID")
    private Integer playlist_id;
    private String playlist_name;



    @OneToMany
    private List<Song> songs;



    public void addSongs(Song song) {
        this.songs.add(song);
    }
    public void deleteSongs(Song song) {
        this.songs.remove(song);
    }

    public String toString() {
        return "Playlist [playlist_id=" + playlist_id + ", playlist_name=" + playlist_name + ", songs=" + songs + "]";
    }

}
