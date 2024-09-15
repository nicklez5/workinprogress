package com.spotify11.demo.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @OneToMany(cascade=CascadeType.ALL , fetch = FetchType.EAGER)
    private Set<Song> songs = new HashSet<>();


    public String toString(){
        getSongs().forEach(System.out::println);
        return getSongs().toString();

    }

}
