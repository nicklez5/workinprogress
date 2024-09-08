package com.spotify11.demo.controller;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;



import com.spotify11.demo.services.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("")
public class UserController {


    @Autowired
    private PlaylistRepo playlistRepo;
    @Autowired
    private SongRepo songRepo;
    public UserController(UserRepo userRepo, UserService service, SongRepo songRepo) {

        this.userRepo = userRepo;
        this.service = service;
        this.songRepo = songRepo;
    }
    @Autowired
    private final UserService service;


    @Autowired
    private final UserRepo userRepo;

    @GetMapping("/")
    public String greet(HttpServletRequest request){
        return "Welcome to Jackson " + request.getSession().getId();
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user) throws UserException {
        return service.register(user);

    }
    @GetMapping("/info")
    public Users infoUser(@RequestParam("username") String username) throws UserException {
        return service.readUser(username);
    }


    @GetMapping("/all")
    public List<Users> getAllUsers() throws UserException {
        return service.getAllUser();
    }

    @PutMapping("/update/")
    public Users updateUser(@RequestParam("username") String username,@RequestParam("password") String user_password, @RequestParam("role") String user_role, @RequestParam("email") String user_email) throws CurrentUserException{
        return service.updateUser(username,user_password,user_role,user_email);

    }

    @DeleteMapping("/deleteUser/{user_id}")
    public Users deleteUser(@RequestParam("username") String username, @PathVariable("user_id") Integer user_id) throws CurrentUserException, UserException {
         return service.deleteUser(username,user_id);

    }

    @GetMapping("/readUser")
    public Users readUser(@RequestParam("username") String username) throws CurrentUserException{
        return service.readUser(username);

    }
    @PostMapping("/login")
    public String logIn(@RequestBody Users user) throws CurrentUserException{
        return service.verify(user);

    }
    @PostMapping("/createPlaylist")
    public String createPlaylist(@RequestBody Playlist entity) throws CurrentUserException{
        Playlist playlist = new Playlist(entity.getName(),entity.getId());
        playlist = playlistRepo.save(playlist);
        return "Playlist name: " + playlist.getName() + " created";
    }
//    @PostMapping("/createPlaylist_with_songs")
//    public String createPlaylistWithSongs(@RequestBody Playlist entity) throws CurrentUserException{
//        Playlist playlist = new Playlist(entity.getName(),entity.getId());
//        Set<Song> songs = new HashSet<>();
//        Song song1 = this.songRepo.getReferenceById(12);
//        Song song2 = this.songRepo.getReferenceById(11);
//        Song song3 = this.songRepo.getReferenceById(10);
//        songs.add(song1);
//        songs.add(song2);
//        songs.add(song3);
//
//        playlist.setSongs(songs);
//        playlistRepo.save(playlist);
//        return "Playlist name: " + playlist.getName() + " created";

    @GetMapping("/getPlaylist/{playlist_id")
    public String getPlaylist(@PathVariable("playlist_id") Integer playlist_id) throws CurrentUserException{
        Playlist playlist = this.playlistRepo.getById(playlist_id);
        System.out.println("Playlist details: " + playlist.toString() + "\n");
        System.out.println("Song details: " + playlist.getSongs().toString() + "\n");
        return "Playlist name: " + playlist.getName() + " created";
    }
//    @PostMapping("/login")
//    public CurrentUserSession logIn(@RequestBody Login logIn) throws CurrentUserException{
//        return service.logIn(logIn);
//
//    }
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request){
//         String token = extractTokenFromRequest(request);
//         blacklist.addToBlacklist(token);
//         return ResponseEntity.ok("Logged out successfully");
//
//    }
//    public String extractTokenFromRequest(HttpServletRequest request){
//        String authorizationHeader = request.getHeader("Authorization");
//        if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }

    

}
