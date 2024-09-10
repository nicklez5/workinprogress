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

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("song")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Transactional
    @GetMapping("/{id}/info")
    public Song getSong(@PathVariable("id") int id, @RequestParam("username") String username) throws UserException, SongException {
        return songService.getSong(id,username);
    }
    @Transactional
    @PostMapping("/upload")
    public UploadFileResponse uploadSong(@RequestParam("username") String username, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws Exception {
        return songService.createSong(title,artist,file,username);

    }
    @Transactional
    @PutMapping("/editSong/{song_id}")
    public Song editSong(@PathVariable("song_id") Integer song_id, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws UserException, SongException, IOException {
        return songService.updateSong(title,artist,file,song_id,username);

    }
    @Transactional
    @DeleteMapping("/deleteSong/{song_id}")
    public Song delete_song(@PathVariable("song_id") Integer song_id, @RequestParam("username") String username) throws  UserException, SongException {
        return songService.deleteSong(song_id,username);

    }
    @Transactional
    @GetMapping("/all")
    public List<Song> getAllSongs(@RequestParam("username") String username) throws  UserException, SongException {
        return songService.getAllSongs(username);

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
