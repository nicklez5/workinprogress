package com.spotify11.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.File;
import java.time.Duration;

@Entity
@Table(name="songs")
public class Song {
    @Id
    private int song_id;
    @Column
    private String title;
    private String artist;
    private Duration duration;
    private File song_file;
    public Song(){

    }
    public Song(int song_id, String title, String artist, File song_file) {
        this.song_id = song_id;
        this.title = title;
        this.artist = artist;
        this.song_file = song_file;

    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public File getSong_file() {
        return song_file;
    }

    public void setSong_file(File song_file) {
        this.song_file = song_file;
    }

    @Override
    public String toString(){
        return "Song(Song ID: " + this.song_id + "  " + "Title: " + this.title + "  " +  "Artist: " + this.artist + "  " + "Duration: " + this.duration.toString() + "  " + "File: " + this.song_file.getName();
    }
}
