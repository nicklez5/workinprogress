package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.PlaylistException;

import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.services.PlaylistService;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("playlist")
public class PlaylistController {


    private final PlaylistService playlistService;




    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;


    }


    // ADD SONG
    @Transactional
    @PostMapping("/addSongToPlaylist/{username_id}")
    public Playlist addSongForPlaylist(@RequestBody Song entity, @PathVariable(name = "username_id") String username_id) throws Exception {
        try{
            return playlistService.addSong(entity.getId(), username_id);
        } catch (Exception e) {
            throw new Exception("Song name: " + entity.getTitle() + "could not be found");
        }

    }
    @Transactional
    @DeleteMapping("/removeSongFromPlaylist/{username_id}")
    public Playlist removeSongFromPlaylist(@RequestBody Song entity, @PathVariable(name = "username_id") String username_id) throws Exception {
        try{
            return playlistService.removeSong(entity.getId(), username_id );
        } catch (Exception e) {
            throw new Exception("Song name: " + entity.getTitle() + "could not be found");
        }

    }
    @Transactional
    @GetMapping(value = "/getSongs")
    public String getSongs(@RequestParam("username") String username) throws Exception {
        try{
            return playlistService.getSongs(username);
        } catch (Exception e) {
            throw new Exception("Could not find user with username: " + username);
        }

    }


    // DELETE
    @Transactional
    @DeleteMapping("/deletePlaylist")
    public String deletePlaylist(@RequestParam("username") String username) throws PlaylistException, UserException{
        return playlistService.deletePlaylist(username);

    }

    // ADD
    @Transactional
    @PostMapping("/createPlaylist")
    public Playlist createPlaylist(@RequestParam("username") String username, @RequestParam("playlist_name") String playlist_name) throws  UserException {
        return playlistService.createPlaylist(username,playlist_name);

    }
    // READ
    @GetMapping("/readPlaylist")
    public Playlist readPlaylist(@RequestParam("username") String username) throws PlaylistException, UserException{
        return playlistService.readPlaylist(username);

    }
    //RENAME
    @Transactional
    @PutMapping("/renamePlaylist")
    public String renamePlaylist(@RequestParam("username") String username, @RequestParam("playlist_name") String playlist_name) throws UserException, PlaylistException {
        return playlistService.renamePlaylist(username,playlist_name);

    }
    // CLEAR
    @Transactional
    @DeleteMapping("/clearPlaylist")
    public String clearPlaylist(@RequestParam("username") String username) throws UserException, PlaylistException {
        return playlistService.clearPlaylist(username);

    }

    // GET A PLAYLIST
    @Transactional
    @GetMapping("/getPlaylist")
    public String getPlaylist(@RequestParam("username") String username) throws UserException, PlaylistException {
        return playlistService.getPlaylist(username);

    }




}
