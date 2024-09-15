package com.spotify11.demo.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.*;

@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @OneToMany(cascade=CascadeType.ALL , fetch = FetchType.EAGER)
    private List<Song> songs = new LinkedList<>();


    public String toString(){
        getSongs().forEach(System.out::println);
        return getSongs().toString();

    }

}
