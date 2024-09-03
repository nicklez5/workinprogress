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

    @GetMapping("/info/{uuId}")
    public ResponseEntity<Song> getSong(@PathVariable("uuId") String uuId, @RequestBody String title) throws UserException, SongException {
        Song xyz = songService.getSong(title,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        httpHeaders.setContentType(MediaType.valueOf("audio/mpeg"));
        return new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
    }
    @PostMapping("/upload")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws UserException, SongException, IOException {
        String fileName = songService.uploadFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
        return new UploadFileResponse(fileName, fileDownloadUri,file.getContentType(),file.getSize());
    }
    @PostMapping("/upload/{uuId}")
    public ResponseEntity<UploadFileResponse> uploadSong(@PathVariable("uuId") String uuId, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file) throws Exception {
        UploadFileResponse xyz = songService.createSong(title,artist,file,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        return new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);

    }
    @PostMapping("/uploadMultipleFiles/{uuId}")
    public List<UploadFileResponse> uploadFileResponses(@RequestParam("files") MultipartFile[] files){
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        return uploadFile(file);
                    } catch (UserException e) {
                        throw new RuntimeException(e);
                    } catch (SongException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
    @PutMapping("/edit/{uuId}/{id}")
    public ResponseEntity<Song> editSong(@PathVariable("uuId") String uuId,@PathVariable("id") Integer id, @RequestParam("title") String title, @RequestParam("artist") String artist, @RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws UserException, SongException, IOException {
        Song xyz = songService.updateSong(title,artist,file,id,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        httpHeaders.setContentType(MediaType.parseMediaType(type));
        httpHeaders.setContentLength(file.getSize());
        return new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{uuId}/{id}")
    public ResponseEntity<?> delete_song(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws  UserException, SongException {
        Song xyz = songService.getSong(id, uuId);
        String deleted_song = xyz.getTitle();
        songService.deleteSong(xyz,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info","Song" + deleted_song);
        return new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
    }
    @GetMapping("/all/{uuId}")
    public ResponseEntity<List<Song>> getAllSongs(@PathVariable("uuId") String uuId) throws  UserException, SongException {
        List<Song> xyz = songService.getAllSongs(uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", xyz.toString());
        return new ResponseEntity<>(xyz,httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{uuId}/")
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
