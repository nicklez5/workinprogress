package com.spotify11.demo.controller;


import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.LibraryService;
import com.spotify11.demo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library")
public class LibraryController {

    private final LibraryService libraryService;
    private final SongService songService;
    public LibraryController(LibraryService libraryService, SongService songService) {
        this.libraryService = libraryService;
        this.songService = songService;
    }




    @PostMapping("/addSong/{song_id}")
    public Library addSong(@PathVariable("song_id") int song_id, @RequestParam("username") String username) throws SongException, UserException {
        Song song1 = this.songService.getSong(song_id,username);
        if(song1 != null){
            return libraryService.addSong(song1, username);
        }else{
            throw new SongException("Song Not Found");
        }


    }

    @DeleteMapping("/deleteSong/{song_id}")
    public Library deleteSong(@PathVariable("song_id") int song_id, @RequestParam("username") String username) throws SongException, UserException {
        Song song1 = this.songService.getSong(song_id,username);
        if(song1 != null){
            return libraryService.deleteSong(song1,username);
        }else{
            throw new SongException("Song not Found");
        }



    }


    @GetMapping("/info")
    public Library getLibrary(@RequestParam("username") String username) throws  LibraryException {
        return libraryService.getLibrary(username);

    }
    @DeleteMapping("/clear")
    public Library clearLibrary(@RequestParam("username") String username) throws  LibraryException {
        return libraryService.clearLibrary(username);

    }

}

