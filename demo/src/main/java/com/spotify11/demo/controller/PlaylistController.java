package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import com.spotify11.demo.services.PlaylistService;
import com.spotify11.demo.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("playlist")
public class PlaylistController {


    private final PlaylistService playlistService;

    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;
    private final UserRepo userRepo;
    public PlaylistController(PlaylistService playlistService, PlaylistRepo playlistRepo, SongRepo songRepo, UserRepo userRepo) {
        this.playlistService = playlistService;

        this.playlistRepo = playlistRepo;
        this.songRepo = songRepo;

        this.userRepo = userRepo;
    }


    // ADD SONG
    @PostMapping("/addSongToPlaylist/{playlist_id}")
    public String addSongForPlaylist(@RequestBody Song entity, @PathVariable(name = "playlist_id") Integer playlist_id)  {
        Song song1 = new Song(entity.getId(), entity.getTitle(), entity.getArtist(), entity.getFileDownloadUri(),entity.getFilename());
        song1 = songRepo.save(song1);

        Playlist playlist1 = this.playlistRepo.getReferenceById(playlist_id);
        playlist1.getSongs().add(song1);
        playlistRepo.save(playlist1);
        return "Song: " + entity.getTitle() + " has been added";
    }
    @DeleteMapping("/removeSongFromPlaylist/{playlist_id}")
    public String removeSongFromPlaylist(@RequestBody Song entity, @PathVariable(name = "playlist_id") Integer playlist_id)  {
        Playlist playlist1 = this.playlistRepo.findById(playlist_id).isPresent() ? this.playlistRepo.findById(playlist_id).get() : null;
        playlist1.getSongs().remove(entity);
        playlistRepo.save(playlist1);
        return "Song: " + entity.getTitle() + " has been removed";
    }
    @GetMapping(value = "/getSongs/{playlist_id}")
    public String getSongs(@PathVariable(name = "playlist_id") Integer playlist_id) {
        Playlist playlist1 = this.playlistRepo.findById(playlist_id).isPresent() ? this.playlistRepo.findById(playlist_id).get() : null;
        Set<Song> songs = playlist1.getSongs();
        return songs.toString();
    }
//    @PostMapping(value= "/assignSongsToPlaylist/{playlist_id}")
//    public String assignSongsToPlaylist( @PathVariable(name = "playlist_id") Integer playlist_id)  {
//        Playlist playlist1 = this.playlistRepo.getReferenceById(playlist_id);
//        Set<Song> songs = playlist1.getSongs();
//        Song song1 = this.songRepo.getReferenceById(3);
//        Song song2 = this.songRepo.getReferenceById(8);
//        Song song3 = this.songRepo.getReferenceById(10);
//        playlist1.setSongs(songs);
//        playlist1 = playlistRepo.save(playlist1);
//        System.out.println("Songs assigned to playlist." + "\n");
//        return "Songs assigned to playlist." + "\n";
//    }

    // DELETE
    @DeleteMapping("/deletePlaylist/{playlist_id}")
    public String deletePlaylist(@RequestParam("username") String username, @RequestParam("playlist_name") String playlist_name) throws PlaylistException, UserException{
        Playlist playlist1 = playlistRepo.findByName(playlist_name);
        Users xyz = userRepo.findByUsername(username);
        xyz.removePlaylist(playlist1);
        playlist1.getSongs().clear();
        playlistRepo.save(playlist1);
        userRepo.save(xyz);

        return "Playlist name:" + playlist1.getName() + " has been deleted";
    }

    // ADD
    @PostMapping("/createPlaylist")
    public Playlist createPlaylist(@RequestParam("username") String username, @RequestParam("playlist_name") String playlist_name) throws  UserException {
        return playlistService.createPlaylist(username,playlist_name);


    }
    // READ
    @GetMapping("/readPlaylist/{id}")
    public Playlist readPlaylist(@RequestParam("username") String username, @PathVariable("id") Integer id) throws PlaylistException, UserException{
        return playlistService.readPlaylist(username,id);

    }
    //RENAME
    @PutMapping("/renamePlaylist/{playlist_id}")
    public Playlist renamePlaylist(@RequestParam("username") String username, @PathVariable("playlist_id") Integer playlist_id, @RequestParam("playlist_name") String playlist_name) throws UserException, PlaylistException {
        return playlistService.renamePlaylist(username,playlist_id,playlist_name);

    }
    // CLEAR
    @DeleteMapping("/clearPlaylist/{playlist_id}")
    public Playlist clearPlaylist(@RequestParam("username") String username, @PathVariable("playlist_id") Integer playlist_id) throws UserException, PlaylistException {
        return playlistService.clearPlaylist(username,playlist_id);

    }

    // GET A PLAYLIST
    @GetMapping("/getPlaylist/{playlist_id}")
    public Playlist getPlaylist(@RequestParam("username") String username, @PathVariable("playlist_id") Integer playlist_id) throws UserException, PlaylistException {
        return playlistService.getPlaylist(username,playlist_id);

    }

    // GET ALL PLAYLIST
    @GetMapping("/getAllPlaylists")
    public Set<Playlist> getAllPlaylist(@RequestParam("username") String username) throws UserException, PlaylistException {
        return playlistService.getAllPlaylists(username);

    }



}
