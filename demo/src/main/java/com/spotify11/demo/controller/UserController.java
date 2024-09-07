package com.spotify11.demo.controller;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Login;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.UserRepo;


import com.spotify11.demo.services.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {


    public UserController(UserRepo userRepo,  UserService service) {
        this.userRepo = userRepo;
        this.service = service;

    }
    @Autowired
    private final UserService service;


    @Autowired
    private final UserRepo userRepo;

    @GetMapping("/")
    public String greet(HttpServletRequest request){
        return "Welcome to Jackson " + request.getSession().getId();
    }

    @PostMapping("/add")
    public Users addUser(@RequestBody Users user) throws UserException {
        return service.addUser(user);

    }
    @GetMapping("/info")
    public String infoUser(HttpServletRequest request) throws UserException {
        return request.getSession().getId();
    }


    @GetMapping("/all")
    public List<Users> getAllUsers() throws UserException {
        return service.getAllUser();
    }

    @PutMapping("/{uuId}/update")
    public Users updateUser(@RequestBody Users user, @PathVariable("uuId") String uuId) throws CurrentUserException{
        return service.updateUser(user,uuId);

    }

    @DeleteMapping("/{uuId}/deleteUser/{id}")
    public Users deleteUser(@PathVariable("uuId") String uuId, @PathVariable("id") Integer id) throws CurrentUserException, UserException {
         return service.deleteUser(uuId,id);

    }

    @GetMapping("/{uuId}/readUser")
    public Users readUser(@PathVariable("uuId") String uuId) throws CurrentUserException{
        return service.readUser(uuId);

    }
    @PostMapping("/login")
    public String logIn(@RequestBody Users user) throws CurrentUserException{
        return service.verify(user);

    }
//    @PostMapping("/login")
//    public CurrentUserSession logIn(@RequestBody Login logIn) throws CurrentUserException{
//        return service.logIn(logIn);
//
//    }
    @DeleteMapping("/logout/{uuId}")
    public String logOut(@PathVariable("uuId") String uuId) throws CurrentUserException{
        return service.logOut(uuId);

    }
    

}
