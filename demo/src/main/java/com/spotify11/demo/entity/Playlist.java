package com.spotify11.demo.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id", unique = true)
    private Integer id;

    @Column(name = "name")
    private String name;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Song> songs = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true, nullable = false)
    private User user;


}
