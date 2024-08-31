package com.spotify11.demo.controller;


import java.util.Optional;

import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.services.LibraryService;

@RestController
@RequestMapping("library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @PostMapping("/{uuId}/add")
    public ResponseEntity<String> addSong(@RequestBody Song song,@PathVariable("uuId") String uuId) throws SongException, CurrentUserException, LibraryException {
        if(song != null){
            Library library = libraryService.addSong(song, uuId);
            String message = song.toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }

    }

    private User getUser(String uuId){
        return getUser(uuId, sessionRepo, userRepo);
    }

}

