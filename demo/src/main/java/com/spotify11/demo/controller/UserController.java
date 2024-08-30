package com.spotify11.demo.controller;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Login;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.UserRepo;
import com.spotify11.demo.services.UserService;
//import com.spotify11.demo.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) throws UserException {
        User user1 = userService.addUser(user);
        return new ResponseEntity<User>(user1, HttpStatus.CREATED);
    }
//    @PostMapping("/add")
//    public ResponseEntity<String> addUser() throws UserException {
//        String user1 = userService.addUser();
//        return new ResponseEntity<String>(user1, HttpStatus.CREATED);

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() throws UserException {
        List<User> userList = userRepo.findAll();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }
//    @PostMapping("/add")
//    public ResponseEntity<User> addUser(@RequestBody User user) throws UserException {
//        User user1 = userService.addUser(user);
//        return new ResponseEntity<User>(user1, HttpStatus.CREATED);
//    }
//
    @PutMapping("/update/{uuId}/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("uuId") String uuId,Integer id) throws CurrentUserException{
        User user1 = userService.updateUser(user,uuId);
        return new ResponseEntity<User>(user1, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{uuId}/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws CurrentUserException, UserException {
        User user1 = userService.deleteUser(uuId,id);
        return new ResponseEntity<User>(user1, HttpStatus.OK);
    }

    @GetMapping("/read/{uuId}")
    public ResponseEntity<User> readUser(@PathVariable("uuId") String uuId) throws CurrentUserException{
        User user1 = userService.readUser(uuId);
        return new ResponseEntity<User>(user1, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CurrentUserSession> logIn(@RequestBody Login logIn) throws CurrentUserException{
        CurrentUserSession session = userService.logIn(logIn);
        return new ResponseEntity<CurrentUserSession>(session,HttpStatus.OK);
    }
    @DeleteMapping("/logout/{uuId}")
    public ResponseEntity<String> logOut(@PathVariable("uuId") String uuId) throws CurrentUserException{
        String message = userService.logOut(uuId);
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    

}
