package com.spotify11.demo.repo;

import com.spotify11.demo.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;



public interface SongRepo extends CrudRepository<Song,Integer> {
    public Optional<Song>  findByTitle(String title);
    
}
