package com.spotify11.demo.controller;

import com.spotify11.demo.dtos.RegisterUserDto;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.UserRepository;

import com.spotify11.demo.services.UserService;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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
//    @Transactional
//    @PostMapping("/register")
//    public ResponseEntity<User> register(String fullName, String password, String email) throws UserException {
//        User xyz2 = userService.addUser(fullName,password,email);
//        return ResponseEntity.ok(xyz2);
//
//    }
    @Transactional
    @GetMapping("/info")
    public ResponseEntity<User> infoUser(@RequestParam("email") String email) throws UserException {
        User user1 =  userService.readUser(email);
        return ResponseEntity.ok(user1);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("username") String username,@RequestParam("password") String user_password, @RequestParam("email") String user_email) throws UserException {
        User user1 =  userService.updateUser(username,user_password,user_email);
        return ResponseEntity.ok(user1);
    }


    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestParam("email") String email) throws UserException {
        User user1  = userService.deleteUser(email);
        return ResponseEntity.ok(user1);
    }
    @Transactional
    @GetMapping("/read")
    public ResponseEntity<User> readUser(@RequestParam("email") String email) throws UserException{
        User user1 = userService.readUser(email);
        return ResponseEntity.ok(user1);
    }


}

