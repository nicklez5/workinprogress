package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.PlaylistException;

import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.services.PlaylistService;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // ADD SONG
    @Transactional
    @PostMapping("/addSongToPlaylist/{email}")
    public ResponseEntity<Playlist> addSongForPlaylist(@RequestBody Song entity, @PathVariable(name = "email") String email) throws Exception {
        try{
            Playlist playlist1 = playlistService.addSong(entity.getId(), email);
            return ResponseEntity.ok(playlist1);
        } catch (Exception e) {
            throw new Exception("Song name: " + entity.getTitle() + "could not be found");
        }

    }
    @Transactional
    @DeleteMapping("/removeSongFromPlaylist/{email}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@RequestBody Song entity, @PathVariable(name = "email") String email) throws Exception {
        try{
            Playlist playlist1 = playlistService.removeSong(entity.getId(), email );
            return ResponseEntity.ok(playlist1);
        } catch (Exception e) {
            throw new Exception("Song name: " + entity.getTitle() + "could not be found");
        }

    }
    @Transactional
    @GetMapping(value = "/getSongs")
    public ResponseEntity<String> getSongs(@RequestParam("email") String email) throws Exception {
        try{
            String str1 = playlistService.getSongs(email);
            return ResponseEntity.ok(str1);
        } catch (Exception e) {
            throw new Exception("Could not find user with email: " + email);
        }

    }


    // DELETE
    @Transactional
    @DeleteMapping("/deletePlaylist")
    public ResponseEntity<Playlist> deletePlaylist(@RequestParam("email") String email) throws PlaylistException, UserException{
        Playlist pl1 = playlistService.deletePlaylist(email);
        return ResponseEntity.ok(pl1);
    }

    // ADD
    @Transactional
    @PostMapping("/createPlaylist")
    public ResponseEntity<Playlist> createPlaylist(@RequestParam("email") String email, @RequestParam("playlist_name") String playlist_name) throws  UserException {
        Playlist pl2 = playlistService.createPlaylist(email,playlist_name);
        return ResponseEntity.ok(pl2);
    }
    // READ
    @GetMapping("/readPlaylist")
    public ResponseEntity<Playlist> readPlaylist(@RequestParam("email") String email) throws PlaylistException, UserException{
        Playlist pt2 = playlistService.readPlaylist(email);
        return ResponseEntity.ok(pt2);
    }
    //RENAME
    @Transactional
    @PutMapping("/renamePlaylist")
    public ResponseEntity<String> renamePlaylist(@RequestParam("email") String email, @RequestParam("playlist_name") String playlist_name) throws UserException, PlaylistException {
        String str3 = playlistService.renamePlaylist(email,playlist_name);
        return ResponseEntity.ok(str3);
    }
    // CLEAR
    @Transactional
    @DeleteMapping("/clearPlaylist")
    public ResponseEntity<String> clearPlaylist(@RequestParam("email") String email) throws UserException, PlaylistException {
        String str3 = playlistService.clearPlaylist(email);
        return ResponseEntity.ok(str3);
    }

    // GET A PLAYLIST
    @Transactional
    @GetMapping("/getPlaylist")
    public ResponseEntity<String> getPlaylist(@RequestParam("email") String email) throws UserException, PlaylistException {
        String str1 = playlistService.getPlaylist(email);
        return ResponseEntity.ok(str1);
    }




}
