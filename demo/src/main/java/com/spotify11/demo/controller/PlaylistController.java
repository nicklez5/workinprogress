package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.PlaylistException;

import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepository;
import com.spotify11.demo.services.PlaylistService;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;


@CrossOrigin
@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private PlaylistService playlistService;
    private PlaylistRepo playlistRepo;
    private UserRepository userRepository;
    private SongRepo song_repo;




    // ADD SONG
    @Transactional
    @PostMapping("/addSong/{playlist_id}")
    public ResponseEntity<String> addSongForPlaylist(@RequestBody Song entity, @RequestParam("playlist_id") Long playlist_id) throws Exception {
        try{
            String playlist1 = playlistService.addSong(entity,playlist_id);
            return ok(playlist1);
        } catch (Exception e) {
            throw new Exception("Song name: " + entity.getTitle() + "could not be found");
        }

    }
    @Transactional
    @DeleteMapping("/removeSong/{song_id}/fromplaylist/{playlist_id}")
    public ResponseEntity<String> removeSongFromPlaylist(@PathVariable("song_id") Long song_id, @PathVariable(name = "playlist_id") Long playlist_id) throws Exception {
        try{
            if(song_repo.findById(song_id).isPresent()){
                Song song123 = song_repo.findById(song_id).get();
                String playlist1 = playlistService.removeSong(song123,playlist_id);
                return ok(playlist1);
            }
        } catch (Exception e) {
            throw new Exception("Song id: " + song_id  + "could not be found");
        }
        throw new SongException("Song cannot be found");
    }
    @Transactional
    @GetMapping(value = "/getSongs/{playlist_id}")
    public ResponseEntity<Set<Song>> getSongs(@PathVariable("playlist_id") Long playlist_id) throws Exception {
        try{
            if(playlistRepo.findById(playlist_id).isPresent()){
               Set<Song> xyz = playlistRepo.findById(playlist_id).get().getSongs();
               return ok(xyz);
            };
        } catch (Exception e) {
            throw new Exception("Could not find playlist with id: " + playlist_id);
        }
        throw new Exception("Empty songs");

    }


    // DELETE
    @Transactional
    @DeleteMapping("/deletePlaylist/{playlist_id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlist_id") Long playlist_id) throws PlaylistException, UserException{
        playlistService.deletePlaylist(playlist_id);
        return (ResponseEntity<?>) ok();
    }

    // ADD
    @Transactional
    @PostMapping("/createPlaylist/{user_id}/")
    public ResponseEntity<Playlist> createPlaylist(@PathVariable("user_id") Long user_id, @RequestParam("playlist_name") String playlist_name) throws  UserException {
        Playlist play1 = new Playlist(playlistRepo.count(), playlist_name, new HashSet<>(), userRepository.findById(user_id).get());
        return ok(play1);
    }
    // READ
    @GetMapping("/readPlaylist/{playlist_id}")
    public ResponseEntity<?> readPlaylist(@PathVariable("playlist_id") Long playlist_id) throws PlaylistException, UserException{
        playlistService.readPlaylist(playlist_id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.set("info","Playlist songs");
        return ok(headers);
    }
    //RENAME
    @Transactional
    @PutMapping("/renamePlaylist/{playlist_id}")
    public ResponseEntity<String> renamePlaylist(@PathVariable("playlist_id") Long playlist_id, @RequestParam("playlist_name") String playlist_name) throws UserException, PlaylistException {
        playlistService.renamePlaylist(playlist_name,playlist_id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.set("info", "You playlist has been renamed to " + playlist_name);
        return ok().headers(headers).build();


    }
    // CLEAR
    @Transactional
    @DeleteMapping("/clearPlaylist/{playlist_id}")
    public ResponseEntity<String> clearPlaylist(@PathVariable("playlist_id") Long playlist_id) throws UserException, PlaylistException {
        playlistService.clearPlaylist(playlist_id);
        return ok("Playlist has been cleared");
    }

    // GET A PLAYLIST
    @Transactional
    @GetMapping("/getPlaylist/{playlist_id}")
    public ResponseEntity<Set<Song>> getPlaylist(@PathVariable("playlist_id") Long playlist_id) throws UserException, PlaylistException {
        Set<Song> str1 = playlistService.getPlaylist(playlist_id);
        return ok(str1);
    }




}
