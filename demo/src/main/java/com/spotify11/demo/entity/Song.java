package com.spotify11.demo.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Duration;

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



    public Song(Integer id,String title, String artist,Files file) {
        this.song_id = id;
        this.title = title;
        this.artist = artist;
        this.file = file;

    }

    @OneToOne(fetch=FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name="FILE_ID")
    private Files file;





    @Override
    public String toString(){
        return "Song(Song ID: " + this.song_id + "  " + "Title: " + this.title + "  " +  "Artist: " + this.artist + "  " + "Data: " + this.file.getName() + ")";
    }
}
