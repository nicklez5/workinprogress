package com.spotify11.demo.repo;

import com.spotify11.demo.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepo extends JpaRepository<Song,Integer> {
    public Optional<Song> findByTitle(String title);
    
}
