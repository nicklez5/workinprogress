package com.spotify11.demo.entity;

import com.spotify11.demo.exception.SongException;
import jakarta.persistence.*;
import lombok.*;

import com.spotify11.demo.entity.Song;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="library")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "lib_songs", referencedColumnName = "id")
    private List<Song> songs;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy = "library")
    private Users user;

    public void addSong(Song song) {
        this.songs.add(song);
    }
    public void removeSong(Song song) {
        this.songs.remove(song);
    }
    public String toString(){
        return "Library Id: " + id + " Songs: " + songs;
    }

}
