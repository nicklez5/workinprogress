package com.spotify11.demo.controller;

import com.spotify11.demo.UploadFileResponseData;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.response.UploadFileResponse;
import com.spotify11.demo.services.SongService;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/songs")
public class SongController {


    private SongService songService;
    private SongRepo songrepo;

    private final String FOLDER_PATH = "C:/Users/jesus/Downloads/demo(13)/demo/src/main/resources/static";

    @Transactional
    @GetMapping("/info/}")
    public ResponseEntity<String> getSong(@RequestParam("title") String title, @RequestParam("email") String email) throws UserException, SongException {
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
    public ResponseEntity<Song> editSong(@RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file,  @PathVariable("song_id") Long song_id )throws UserException, SongException, IOException {
        Song song2 = songService.updateSong(title, artist, file, song_id);
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
    @GetMapping("/Download/{filename:.+}")
    public ResponseEntity<Resource> downloadSong(@PathVariable String filename) throws IOException {
        Resource file = songService.loadSong(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }



}
