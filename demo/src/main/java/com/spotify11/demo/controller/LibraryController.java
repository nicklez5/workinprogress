package com.spotify11.demo.controller;


import com.spotify11.demo.entity.Library;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.LibraryService;
import com.spotify11.demo.services.SongService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;




    @Transactional
    @PostMapping("/addSong/{library_id}")
    public ResponseEntity<String> addSong(@RequestBody Song song123, @PathVariable("library_id") Integer library_id) throws SongException, UserException, LibraryException {
        String lib1 = libraryService.addSong(song123,library_id);
        return ResponseEntity.ok(lib1);

    }
    @Transactional
    @DeleteMapping("/deleteSong/{library_id}")
    public ResponseEntity<String> deleteSong(@RequestBody Song song123, @PathVariable("library_id") Integer library_id) throws SongException, UserException, LibraryException {
        String lib1 = libraryService.deleteSong(song123,library_id);
        return ResponseEntity.ok(lib1);


    }

    @Transactional
    @GetMapping("/info/{library_id}")
    public ResponseEntity<Set<Song>> getLibrary(@PathVariable("library_id") Integer library_id) throws  LibraryException {
        Set<Song> lib1 = libraryService.getLibrary(library_id);
        return ResponseEntity.ok(lib1);
    }
    @Transactional
    @DeleteMapping("/clear/{library_id}")
    public ResponseEntity<Library> clearLibrary(@PathVariable("library_id") Integer library_id) throws  LibraryException {
        Library lib1 =  libraryService.clearLibrary(library_id);
        return ResponseEntity.ok(lib1);
    }


}

