package com.spotify11.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;

@Repository
public interface LibraryRepo extends JpaRepository<Library,Integer> {
    
    
}
