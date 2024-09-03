package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int song_id;
    private String title;
    private String artist;
    private String fileDownloadUri;


    public Song(Integer id,String title, String artist,final String fileDownloadUri) {
        this.song_id = id;
        this.title = title;
        this.artist = artist;
        this.fileDownloadUri = fileDownloadUri;

    }






    @Override
    public String toString(){
        return "Song(Song ID: " + this.song_id + "  " + "Title: " + this.title + "  " +  "Artist: " + this.artist + "  " + "Data: " + this.fileDownloadUri + ")";
    }
}
