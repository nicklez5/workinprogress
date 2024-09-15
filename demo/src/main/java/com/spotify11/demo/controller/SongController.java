package com.spotify11.demo.controller;

import com.spotify11.demo.UploadFileResponseData;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.response.UploadFileResponse;
import com.spotify11.demo.services.SongService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/songs")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);


    private final SongRepo songrepo;

    private final SongService songService;
    @Autowired
    public SongController(SongService songService,SongRepo songRepo) {
        this.songService = songService;
        this.songrepo = songRepo;
    }

    @Transactional
    @GetMapping("/info/}")
    public ResponseEntity<String> getSong(@RequestParam("title") String title) throws UserException, SongException {
        String str1 = songService.getSong(title);
        return ResponseEntity.ok(str1);
    }
    @Transactional
    @PostMapping("/upload")
    public UploadFileResponse uploadFile(@RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file)  throws Exception {
        return songService.createSong(title, artist, file);

    }
    @Transactional
    @PutMapping("/editSong/{song_id}")
    public ResponseEntity<Song> editSong(@RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file,@PathVariable("song_id") Integer song_id,  @RequestParam("email") String email )throws UserException, SongException, IOException {
        Song song2 = songService.updateSong(title, artist, file,song_id, email);
        songrepo.save(song2);
        return ResponseEntity.ok(song2);
    }


    @Transactional
    @GetMapping("/all")
    public ResponseEntity<List<Song>> getAllSongs() throws UserException, SongException {
        List<Song> lib1 = songService.getAllSongs();
        return ResponseEntity.ok(lib1);
    }

    @Transactional
    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadSong(@RequestParam("filename") String filename, HttpServletRequest request) throws Exception {
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
