package com.spotify11.demo.controller;


import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.services.LibraryService;
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
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }




    @PostMapping("/{uuId}/add")
    public Library addSong(@RequestBody Song song, @PathVariable("uuId") String uuId) throws SongException, CurrentUserException, LibraryException {
          return libraryService.addSong(song, uuId);

    }

    @DeleteMapping("/{uuId}/delete")
    public Library deleteSong(@RequestBody Song song, @PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        return libraryService.deleteSong(uuId, song);


    }


    @GetMapping("/{uuId}/info")
    public Library getLibrary(@PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        return libraryService.getLibrary(uuId);

    }
    @DeleteMapping("/{uuId}/clear")
    public Library clearLibrary(@PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        return libraryService.clearLibrary(uuId);



    }

}

