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
    @Column(name="LIBRARY_ID")
    private int libraryId;


    @OneToMany
    private List<Song> songs;

    public Library(int id) {
        super();
        this.libraryId = id;
        this.songs = new ArrayList<Song>();
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }
    public void removeSong(Song song) {
        this.songs.remove(song);
    }
    public String toString(){
        return "Library Id: " + libraryId + " Songs: " + songs;
    }

}
