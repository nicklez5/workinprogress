package com.spotify11.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotify11.demo.entity.Playlist;

import java.util.List;
import java.util.Optional;


public interface PlaylistRepo extends JpaRepository<Playlist, String>{
    
}
