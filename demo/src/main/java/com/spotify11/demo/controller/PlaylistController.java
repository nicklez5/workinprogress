package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("playlist")
public class PlaylistController {

    @Autowired
    private final PlaylistService playlistService;


    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;

    }


    // ADD SONG
    @PostMapping("/addSong/{uuId}/{id}")
    public ResponseEntity<Playlist> addSong(@RequestBody Song song, @PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws SongException, UserException, CurrentUserException {

        Playlist playlist1 = playlistService.addSong(song,uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Adding: " + song.toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(playlist1);
    }
    // DELETE SONG
    @DeleteMapping("/deleteSong/{uuId}/{id}")
    public ResponseEntity<Playlist> deleteSong(@RequestBody Song song, @PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, SongException, CurrentUserException {
        Playlist playlist1 = playlistService.removeSong(song,uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Deleting: " + song.toString());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(httpHeaders).body(playlist1);
    }

    // DELETE
    @DeleteMapping("/deletePlaylist/{uuId}/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws PlaylistException, UserException, CurrentUserException {
        String playlist1 = playlistService.deletePlaylist(uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Deleting: Playlist id: " + id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(httpHeaders).body(playlist1);
    }

    // ADD
    @PostMapping("/createPlaylist/{id}")
    public ResponseEntity<Playlist> createPlaylist(@PathVariable("id") String id, @RequestBody String name) throws PlaylistException, UserException, CurrentUserException {
        Playlist playlist1 = playlistService.createPlaylist(id, name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Creating playlist: " + playlist1.toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(playlist1);

    }
    // READ
    @GetMapping("/readPlaylist/{uuId}/{id}")
    public ResponseEntity<Playlist> readPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws PlaylistException, UserException, CurrentUserException {
        Playlist playlist1 = playlistService.readPlaylist(uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Reading: " + playlist1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);
    }
    //RENAME
    @PutMapping("/renamePlaylist/{uuId}/{id}")
    public ResponseEntity<Playlist> renamePlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id, @RequestBody String name) throws UserException, CurrentUserException{
        Playlist playlist1 = playlistService.renamePlaylist(uuId,id,name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Renaming playlist: " + playlist1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);
    }
    // CLEAR
    @DeleteMapping("/clearPlaylist/{uuId}/{id}")
    public ResponseEntity<String> clearPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException{
        String playlist1 = playlistService.clearPlaylist(uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", playlist1);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(httpHeaders).body(playlist1);
    }

    // GET A PLAYLIST
    @GetMapping("/getPlaylist/{uuId}/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException {
        Playlist xyz = playlistService.getPlaylist(uuId,id);
        return ResponseEntity.status(HttpStatus.OK).body(xyz);
    }

    // GET ALL PLAYLIST
    @GetMapping("/getPlaylists/{uuId}")
    public ResponseEntity<List<Playlist>> getAllPlaylist(@PathVariable("uuId") String uuId) throws CurrentUserException, UserException {
        List<Playlist> xyz = playlistService.getAllPlaylists(uuId);
        return ResponseEntity.status(HttpStatus.OK).body(xyz);
    }



}
