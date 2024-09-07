package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.response.UploadFileResponse;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.SongService;


import com.spotify11.demo.response.UploadFileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("song")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{uuId}/info")
    public Song getSong(@PathVariable("uuId") String uuId, @RequestParam("title") String title) throws UserException, SongException {
        return songService.getSong(title,uuId);
    }

    @PostMapping("/{uuId}/upload")
    public UploadFileResponse uploadSong(@PathVariable("uuId") String uuId, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws Exception {
        return songService.createSong(title,artist,file,uuId);

    }

    @PutMapping("/{uuId}/editSong/{id}")
    public Song editSong(@PathVariable("uuId") String uuId,@PathVariable("id") Integer id, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws UserException, SongException, IOException {
        return songService.updateSong(title,artist,file,id,uuId);


    }

    @DeleteMapping("/{uuId}/deleteSong/{id}")
    public Song delete_song(@PathVariable("uuId") String uuId, @PathVariable("id") int id) throws  UserException, SongException {
        return songService.deleteSong(id,uuId);

    }
    @GetMapping("/{uuId}/all")
    public List<Song> getAllSongs(@PathVariable("uuId") String uuId) throws  UserException, SongException {
        return songService.getAllSongs(uuId);

    }

    @GetMapping("/{uuId}/downloadFile")
    public ResponseEntity<Resource> downloadSong(@PathVariable("uuId") String uuId, @RequestParam("fileName") String fileName, HttpServletRequest request) throws CurrentUserException, UserException, SongException, IOException {
        Resource resource = songService.loadFileAsResource(fileName);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(IOException e){
            logger.info("Could not determine file type.");
        }
        if(contentType == null){
            logger.info("Could not determine file type.");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }



}
