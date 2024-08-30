package com.spotify11.demo.entity;

import com.spotify11.demo.exception.SongException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.spotify11.demo.entity.Song;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int library_id;
    @Column(unique=false)
    @OneToMany
    private List<Song> songs;
    
    public Library(int id) {
        super();
        this.library_id = id;
        this.songs = new ArrayList<Song>();
    }

    public int getId() {
        return library_id;
    }
    public void setId(int id) {
        this.library_id = id;
    }
    public List<Song> getSongs() {
        return this.songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }
    public void removeSong(Song song) {
        this.songs.remove(song);
    }
    

}
