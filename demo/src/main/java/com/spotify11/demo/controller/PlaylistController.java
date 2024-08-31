package com.spotify11.demo.controller;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.services.PlaylistService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("playlist")
public class PlaylistController {
    
    @Autowired
    private PlaylistService playlistService;
    
    @Autowired
    private PlaylistRepo playlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @PostMapping("/add/{uuId}")
    public ResponseEntity<Playlist> addSong(@RequestBody Song song, @PathVariable("uuId") String uuId) throws SongException, UserException {
        if(song == null) {
            throw new SongException("Song cannot be null");
        }else if(uuId == null) {
            throw new UserException("User cannot be null");
        }
        Playlist playlist1 = playlistService.addSong(song,uuId);
        return ResponseEntity.ok(playlist1);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        List<Playlist> playlistList = playlistRepo.findAll();
        return ResponseEntity.ok(playlistList);
    }

    @DeleteMapping("/delete/{uuId}/{id}")
    public ResponseEntity<Playlist> deletePlaylist(){

    }

    private User getUser(String uuId){
        return getUser(uuId, sessionRepo, userRepo);
    }

    public static User getUser(String uuId, SessionRepo sessionRepo, UserRepo userRepo) {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if(optionalSession.isPresent()){
            CurrentUserSession session = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(session.getUserId());
            return optionalUser.get();
        }
        return null;
    }
}
