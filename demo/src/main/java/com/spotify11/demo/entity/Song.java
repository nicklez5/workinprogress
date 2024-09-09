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
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "file_download_uri")
    private String fileDownloadUri;

    @Column(name = "filename")
    private String filename;

    public Song(String title, String artist, String fileDownloadUri, String filename) {
        this.title = title;
        this.artist = artist;
        this.fileDownloadUri = fileDownloadUri;
        this.filename = filename;
    }

}