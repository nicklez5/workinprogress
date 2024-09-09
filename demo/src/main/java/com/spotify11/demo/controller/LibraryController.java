package com.spotify11.demo.controller;


import com.spotify11.demo.entity.Library;

import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.LibraryService;
import com.spotify11.demo.services.SongService;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("library")
public class LibraryController {

    private final LibraryService libraryService;
    private final SongService songService;
    public LibraryController(LibraryService libraryService, SongService songService) {
        this.libraryService = libraryService;
        this.songService = songService;
    }



    @Transactional
    @PostMapping("/addSong/{title}")
    public Library addSong(@RequestParam("title") String title, @RequestParam("username") String username) throws SongException, UserException {
        return libraryService.addSong(this.songService.getSong(title,username), username);


    }
    @Transactional
    @DeleteMapping("/deleteSong/{title}")
    public Library deleteSong(@RequestParam("title") String title, @RequestParam("username") String username) throws SongException, UserException {
        return libraryService.deleteSong(this.songService.getSong(title,username), username);



    }

    @Transactional
    @GetMapping("/info")
    public Library getLibrary(@RequestParam("username") String username) throws  LibraryException {
        return libraryService.getLibrary(username);

    }
    @Transactional
    @DeleteMapping("/clear")
    public Library clearLibrary(@RequestParam("username") String username) throws  LibraryException {
        return libraryService.clearLibrary(username);

    }

}

