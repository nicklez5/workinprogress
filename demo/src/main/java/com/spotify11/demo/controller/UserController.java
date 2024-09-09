package com.spotify11.demo.controller;

import com.spotify11.demo.entity.*;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;



import com.spotify11.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.List;


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

    @Transactional
    @PostMapping("/register")
    public Users register(@RequestBody Users user) throws UserException {
        return service.register(user);

    }
    @Transactional
    @GetMapping("/info")
    public Users infoUser(@RequestParam("username") String username) throws UserException {
        return service.readUser(username);
    }

    @Transactional
    @GetMapping("/all")
    public List<Users> getAllUsers() throws UserException {
        return service.getAllUser();
    }
    @Transactional
    @PutMapping("/update")
    public Users updateUser(@RequestParam("username") String username,@RequestParam("password") String user_password, @RequestParam("role") String user_role, @RequestParam("email") String user_email) throws UserException {
        return service.updateUser(username,user_password,user_role,user_email);

    }
    @Transactional
    @DeleteMapping("/deleteUser/{user_id}")
    public Users deleteUser(@RequestParam("username") String username, @PathVariable("user_id") Integer user_id) throws  UserException {
         return service.deleteUser(username,user_id);

    }
    @Transactional
    @GetMapping("/readUser")
    public Users readUser(@RequestParam("username") String username) throws UserException{
        return service.readUser(username);

    }
    @Transactional
    @PostMapping("/login")
    public String logIn(@RequestBody Users user){
        return service.verify(user);

    }


    

}
