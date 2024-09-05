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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    public UserController(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;

    }
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepo userRepo;

    @Validated
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) throws UserException {
        User user1 = userService.addUser(user);

        String xyz = user1.toString();
        return new ResponseEntity<String>(xyz, HttpStatus.CREATED);
    }
//    @PostMapping("/add")
//    public ResponseEntity<String> addUser() throws UserException {
//        String user1 = userService.addUser();
//        return new ResponseEntity<String>(user1, HttpStatus.CREATED);

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() throws UserException {
        List<User> userList = (List<User>) userRepo.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
//    @PostMapping("/add")
//    public ResponseEntity<User> addUser(@RequestBody User user) throws UserException {
//        User user1 = userService.addUser(user);
//        return new ResponseEntity<User>(user1, HttpStatus.CREATED);
//    }
//
    @PutMapping("/{uuId}/update")
    public ResponseEntity<String> updateUser(@RequestBody User user, @PathVariable("uuId") String uuId) throws CurrentUserException{
        User user1 = userService.updateUser(user,uuId);
        String xyz = user1.toString();
        return new ResponseEntity<String>(xyz, HttpStatus.OK);
    }

    @DeleteMapping("/{uuId}/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws CurrentUserException, UserException {
        User user1 = userService.deleteUser(uuId,id);
        String xyz = user1.toString();
        xyz += "has been deleted";
        return new ResponseEntity<String>(xyz, HttpStatus.OK);
    }

    @GetMapping("/{uuId}/readUser")
    public ResponseEntity<String> readUser(@PathVariable("uuId") String uuId) throws CurrentUserException{
        User user1 = userService.readUser(uuId);
        String xyz = user1.toString();
        return new ResponseEntity<String>(xyz, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CurrentUserSession> logIn(@RequestBody Login logIn) throws CurrentUserException{
        CurrentUserSession session = userService.logIn(logIn);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
    @DeleteMapping("/logout/{uuId}")
    public ResponseEntity<String> logOut(@PathVariable("uuId") String uuId) throws CurrentUserException{
        String message = userService.logOut(uuId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    

}
