package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("song")
public class SongController {

    @Autowired
    private final SongService songService;
    private final SongRepo songRepo;
    public SongController(SongService songService, SongRepo songRepo) {
        this.songService = songService;
        this.songRepo = songRepo;

    }
    @GetMapping("/info/{uuId}")
    public ResponseEntity<Song> getSong(@PathVariable("uuId") String uuId, @RequestBody String title) throws CurrentUserException, UserException, SongException {
        Song xyz = songService.getSong(title,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        ResponseEntity<Song> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }
    @PostMapping("/upload/{uuId}")
    public ResponseEntity<Song> uploadSong(@PathVariable("uuId") String uuId,@RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws Exception {
        Song xyz = songService.createSong(title,artist,file,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        ResponseEntity<Song> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }
    @PutMapping("/edit/{uuId}/{id}")
    public ResponseEntity<Song> editSong(@PathVariable("uuId") String uuId,@PathVariable("id") Integer id, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws UserException, SongException, IOException {
        Song xyz = songService.updateSong(title,artist,file,id,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        ResponseEntity<Song> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }

    @GetMapping("/view_song/{uuId}/{id}")
    public ResponseEntity<Song> read_song(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws CurrentUserException, UserException, SongException {
        Song xyz =  songService.getSong(id,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        ResponseEntity<Song> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }
    @DeleteMapping("/delete/{uuId}/{id}")
    public ResponseEntity<?> delete_song(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws CurrentUserException, UserException, SongException {
        Song xyz = songService.getSong(id, uuId);
        String deleted_song = xyz.getTitle();
        songService.deleteSong(xyz,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info","Song: " + deleted_song + " deleted");
        ResponseEntity<?> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }
    @GetMapping("/all/{uuId}")
    public ResponseEntity<List<Song>> getAllSongs(@PathVariable("uuId") String uuId) throws CurrentUserException, UserException, SongException {
        List<Song> xyz = songService.getAllSongs(uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        ResponseEntity<List<Song>> entity = new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
        return entity;
    }
    @GetMapping("/download/")
    public ResponseEntity<byte[]> downloadSong(@RequestParam("filename") String filename) throws CurrentUserException, UserException, SongException, IOException {
        byte[] xyz = songService.downloadFileFromLibrary(filename);
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<byte[]> entity = new ResponseEntity<>(xyz, HttpStatus.OK);
        return entity;

    }



}
