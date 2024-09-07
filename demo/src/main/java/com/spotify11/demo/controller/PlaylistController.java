package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.services.PlaylistService;
import com.spotify11.demo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("playlist")
public class PlaylistController {


    private final PlaylistService playlistService;


    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;

    }


    // ADD SONG
    @PostMapping("/{uuId}/{id2}/addSong/{song_id}")
    public Playlist addSong(@PathVariable("uuId") String uuId, @PathVariable("id2") Integer id2, @PathVariable("song_id") Integer song_id) throws SongException, UserException, CurrentUserException {
        song_id = song_id - 1;
        return playlistService.addSong(song_id,uuId,id2);

    }
    // DELETE SONG
    @DeleteMapping("/{uuId}/{id2}/deleteSong/{song_id}")
    public Playlist deleteSong(@PathVariable("id2") Integer id2, @PathVariable("uuId") String uuId, @PathVariable("song_id") Integer song_id) throws UserException, SongException, CurrentUserException {
        song_id = song_id - 1;
        return playlistService.removeSong(song_id,uuId,id2);

    }

    // DELETE
    @DeleteMapping("/{uuId}/deletePlaylist")
    public Playlist deletePlaylist(@PathVariable("uuId") String uuId, @RequestParam("name") String name) throws PlaylistException, UserException, CurrentUserException {
        return playlistService.deletePlaylist(uuId,name);


    }

    // ADD
    @PostMapping("/{uuId}/createPlaylist")
    public Playlist createPlaylist(@PathVariable("uuId") String uuId, @RequestParam("playlist_name") String playlist_name) throws PlaylistException, UserException, CurrentUserException {
        return playlistService.createPlaylist(uuId,playlist_name);




    }
    // READ
    @GetMapping("/{uuId}/readPlaylist/{id}")
    public Playlist readPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws PlaylistException, UserException, CurrentUserException {
        return playlistService.readPlaylist(uuId,id);

    }
    //RENAME
    @PutMapping("/{uuId}/renamePlaylist/{id}")
    public Playlist renamePlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id, @RequestParam("name") String name) throws UserException, CurrentUserException{
        return playlistService.renamePlaylist(uuId,id,name);

    }
    // CLEAR
    @DeleteMapping("/{uuId}/clearPlaylist/{id}")
    public Playlist clearPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException{
        return playlistService.clearPlaylist(uuId,id);

    }

    // GET A PLAYLIST
    @GetMapping("/{uuId}/getPlaylist/{id}")
    public Playlist getPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException {
        return playlistService.getPlaylist(uuId,id);

    }

    // GET ALL PLAYLIST
    @GetMapping("/{uuId}/getAllPlaylists")
    public List<Playlist> getAllPlaylist(@PathVariable("uuId") String uuId) throws CurrentUserException, UserException {
        return playlistService.getAllPlaylists(uuId);

    }



}
