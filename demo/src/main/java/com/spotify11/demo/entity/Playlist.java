package com.spotify11.demo.entity;

import jakarta.persistence.*;

import java.util.List;

@Table(name="playlist")
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playlist_id;

    @Column
    private String playlist_name;

    @Column
    @OneToMany
    private List<Song> songs;

    public Playlist() {
        super();
    }
    public Playlist(Integer id, String title, List<Song> songs) {
        super();
        this.playlist_id = id;
        this.playlist_name = title;
        this.songs = songs;
    }
    public String getPlaylist_name() {
        return this.playlist_name;
    }
    public void setPlaylist_name(String name) {
        this.playlist_name = name;
    }
    public List<Song> getSongs() {
        return songs;
    }
    public void addSongs(Song song) {
        this.songs.add(song);
    }
    public void deleteSongs(Song song) {
        this.songs.remove(song);
    }
    
    public void setId(Integer id) {
        this.playlist_id = id;
    }

    public Integer getId() {
        return playlist_id;
    }

    

}
