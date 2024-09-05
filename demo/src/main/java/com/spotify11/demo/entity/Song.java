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
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "fileDownloadUri")
    private String fileDownloadUri;

    @Column(name = "filename")
    private String filename;



    public Song(String title, String artist, String fileDownloadUri, String filename) {
        this.title = title;
        this.artist = artist;
        this.fileDownloadUri = fileDownloadUri;
        this.filename = filename;
    }




    @Override
    public String toString(){
        return "Song(Song ID: " + this.id + "  " + "Title: " + this.title + "  " +  "Artist: " + this.artist + "  " + "Data: " + this.fileDownloadUri + " " +  "Filename: " + this.filename;
    }
}
