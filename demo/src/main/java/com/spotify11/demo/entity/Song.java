package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

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

    @Column(name = "file_download_uri")
    private String fileDownloadUri;

    @Column(name = "filename")
    private String filename;


    public Song(Integer id, String title, String artist, String fileDownloadUri, String filename) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.fileDownloadUri = fileDownloadUri;
        this.filename = filename;
    }


    //    @Override
//    public String toString(){
//        return "Song(Song ID: " + this.id + "  " + "Title: " + this.title + "  " +  "Artist: " + this.artist + "  " + "Data: " + this.fileDownloadUri + " " +  "Filename: " + this.filename;
//    }
    @Override
    public String toString() {
        return "ID:" + this.id + " " + "Title:" + this.title + " " + "Artist:" + this.artist + " " + "File download url" + this.fileDownloadUri + " " + "File name: " + this.filename + "\n";
    }
}