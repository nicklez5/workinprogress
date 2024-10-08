package com.spotify11.demo.controller;

import com.spotify11.demo.dtos.LoginUserDto;
import com.spotify11.demo.dtos.RegisterUserDto;
import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.UserRepository;

import com.spotify11.demo.response.LoginResponse;
import com.spotify11.demo.services.AuthenticationService;
import com.spotify11.demo.services.JwtService;
import com.spotify11.demo.services.UserService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserService userService, UserRepository userRepository, AuthenticationService authenticationService, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }


    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        registeredUser.setLibrary(new Library());
        List<Playlist> playlists1 = new ArrayList<>();
        registeredUser.setPlaylists(playlists1);
        userRepository.save(registeredUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/info")
    public ResponseEntity<User> authenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getCurrentUser/{user_id}")
    public ResponseEntity<User> getCurrentUser(@PathVariable(value = "user_id") Integer id) {
        User user1 = userRepository.findById(id).get();
        return ResponseEntity.ok(user1);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("fullName") String fullName,@RequestParam("password") String user_password, @RequestParam("email") String user_email) throws UserException {
        User user1 =  userService.updateUser(fullName,user_password,user_email);
        return ResponseEntity.ok(user1);
    }


    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("email") String email) throws UserException {
        String user1 = userService.deleteUser(email);
        return ResponseEntity.ok(user1);
    }
    @Transactional
    @GetMapping("/read")
    public ResponseEntity<User> readUser(@RequestParam("email") String email) throws UserException{
        User user1 = userService.readUser(email);
        return ResponseEntity.ok(user1);
    }


}

