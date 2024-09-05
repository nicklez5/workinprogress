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

    private final SongService songService;

    public PlaylistController(PlaylistService playlistService, SongService songService) {
        this.playlistService = playlistService;

        this.songService = songService;
    }


    // ADD SONG
    @PostMapping("/{uuId}/{id2}/addSong/{song_id}")
    public ResponseEntity<Playlist> addSong(@PathVariable("uuId") String uuId, @PathVariable("id2") Integer id2, @PathVariable("song_id") Integer song_id) throws SongException, UserException, CurrentUserException {
        song_id = song_id - 1;
        Playlist playlist1 = playlistService.addSong(song_id,uuId,id2);
        Song song1 = this.songService.getSong(song_id,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Adding: " + song1.toString() + "to Playlist: " + playlist1.toString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(playlist1);
    }
    // DELETE SONG
    @DeleteMapping("/{uuId}/{id2}/deleteSong/{song_id}")
    public ResponseEntity<Playlist> deleteSong(@PathVariable("id2") Integer id2, @PathVariable("uuId") String uuId, @PathVariable("song_id") Integer song_id) throws UserException, SongException, CurrentUserException {
        song_id = song_id - 1;
        Playlist playlist1 = playlistService.removeSong(song_id,uuId,id2);
        Song song1 = this.songService.getSong(song_id,uuId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Removing Song id: " + song1.toString());
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);

    }

    // DELETE
    @DeleteMapping("/{uuId}/deletePlaylist")
    public ResponseEntity<String> deletePlaylist(@PathVariable("uuId") String uuId, @RequestParam("name") String name) throws PlaylistException, UserException, CurrentUserException {
        String playlist1 = playlistService.deletePlaylist(uuId,name);

        return new ResponseEntity<String>(playlist1,HttpStatus.OK);

    }

    // ADD
    @PostMapping("/{uuId}/createPlaylist")
    public ResponseEntity<String> createPlaylist(@PathVariable("uuId") String uuId, @RequestParam("playlist_name") String playlist_name) throws PlaylistException, UserException, CurrentUserException {
        String playlist2 = playlistService.createPlaylist(uuId,playlist_name);

        return new ResponseEntity<>(playlist2, HttpStatus.CREATED);


    }
    // READ
    @GetMapping("/{uuId}/readPlaylist/{id}")
    public ResponseEntity<Playlist> readPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws PlaylistException, UserException, CurrentUserException {
        Playlist playlist1 = playlistService.readPlaylist(uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Reading: " + playlist1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);
    }
    //RENAME
    @PutMapping("/{uuId}/renamePlaylist/{id}")
    public ResponseEntity<Playlist> renamePlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id, @RequestParam("name") String name) throws UserException, CurrentUserException{
        Playlist playlist1 = playlistService.renamePlaylist(uuId,id,name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", "Renaming playlist: " + playlist1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);
    }
    // CLEAR
    @DeleteMapping("/{uuId}/clearPlaylist/{id}")
    public ResponseEntity<String> clearPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException{
        String playlist1 = playlistService.clearPlaylist(uuId,id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("info", playlist1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(playlist1);
    }

    // GET A PLAYLIST
    @GetMapping("/{uuId}/getPlaylist/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws UserException, CurrentUserException {
        Playlist xyz = playlistService.getPlaylist(uuId,id);
        return ResponseEntity.status(HttpStatus.OK).body(xyz);
    }

    // GET ALL PLAYLIST
    @GetMapping("/{uuId}/getAllPlaylists")
    public ResponseEntity<List<Playlist>> getAllPlaylist(@PathVariable("uuId") String uuId) throws CurrentUserException, UserException {
        List<Playlist> xyz = playlistService.getAllPlaylists(uuId);
        return ResponseEntity.status(HttpStatus.OK).body(xyz);
    }



}
