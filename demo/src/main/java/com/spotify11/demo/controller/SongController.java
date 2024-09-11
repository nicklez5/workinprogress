package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.response.UploadFileResponse;

import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.SongService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/song")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Transactional
    @GetMapping("/info/{id}")
    public ResponseEntity<Song> getSong(@PathVariable("id") int id, @RequestParam("email") String email) throws UserException, SongException {
        Song str1 = songService.getSong(id,email);
        return ResponseEntity.ok(str1);
    }
    @Transactional
    @PostMapping("/upload")
    public UploadFileResponse uploadSong(@RequestParam("email") String email, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws Exception {
        return songService.createSong(title,artist,file,email);

    }
    @Transactional
    @PutMapping("/editSong/{song_id}")
    public ResponseEntity<Song> editSong(@PathVariable("song_id") Integer song_id, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file, @RequestParam("email") String email) throws UserException, SongException, IOException {
        Song song1 = songService.updateSong(title,artist,file,song_id,email);
        return ResponseEntity.ok(song1);
    }
    @Transactional
    @DeleteMapping("/deleteSong/{song_id}")
    public ResponseEntity<Song> delete_song(@PathVariable("song_id") Integer song_id, @RequestParam("email") String email) throws  UserException, SongException {
        Song song1 = songService.deleteSong(song_id,email);
        return ResponseEntity.ok(song1);
    }
    @Transactional
    @GetMapping("/all")
    public ResponseEntity<List<Song>> getAllSongs(@RequestParam("email") String email ) throws UserException, SongException {
        List<Song> lib1 = songService.getAllSongs(email);
        return ResponseEntity.ok(lib1);
    }
    @Transactional
    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadSong(@RequestParam("filename") String filename, HttpServletRequest request) throws IOException {
        Resource resource = songService.loadFileAsResource(filename);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(IOException e){
            logger.info("Could not determine file type.");
        }
        if(contentType == null){
            logger.info("Could not determine file type.");
        }
        assert contentType != null;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }




}
