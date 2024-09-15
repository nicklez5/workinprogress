package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "filename")
    private String filename;

    @Column(name = "fileDownloadUri")
    private URI fileDownloadUri;

    public Song(String title, String artist, String fileName, URI fileDownloadUri1) {
        this.title = title;
        this.artist = artist;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri1;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}