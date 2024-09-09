package com.spotify11.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Playlist(String playlist_name, Integer playlist_id) {
        this.id = playlist_id;
        this.name = playlist_name;

    }

    @OneToMany(cascade=CascadeType.ALL , fetch = FetchType.EAGER)
    private Set<Song> songs;


}
