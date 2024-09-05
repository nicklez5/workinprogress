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
    public ResponseEntity<Library> addSong(@RequestBody Song song, @PathVariable("uuId") String uuId) throws SongException, CurrentUserException, LibraryException {
        Library message = libraryService.addSong(song, uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Added song: " + song.getTitle() + " to library");
        return new ResponseEntity<>(message, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuId}/delete")
    public ResponseEntity<Library> deleteSong(@RequestBody Song song, @PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        Library message = libraryService.deleteSong(uuId, song);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Deleted song: " + song.getTitle() + " from library");
        return new ResponseEntity<>(message, httpHeaders, HttpStatus.CREATED);

    }


    @GetMapping("/{uuId}/info")
    public ResponseEntity<String> getLibrary(@PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        List<Song> xyz = libraryService.getLibrary(uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Library found");
        return new ResponseEntity<>(xyz.toString(), httpHeaders, HttpStatus.OK);
    }
    @DeleteMapping("/{uuId}/clear")
    public ResponseEntity<Library> clearLibrary(@PathVariable("uuId") String uuId) throws CurrentUserException, LibraryException {
        Library xyz = libraryService.clearLibrary(uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Library cleared");
        return new ResponseEntity<>(xyz, httpHeaders, HttpStatus.OK);


}

}

