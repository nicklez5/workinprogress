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
    private Long id;

    @Column(name = "name")
    private String name;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Song> songs = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true, nullable = false)
    private User user;


}
