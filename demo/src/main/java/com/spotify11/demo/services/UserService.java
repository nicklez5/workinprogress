package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;

import java.util.List;

import com.spotify11.demo.exception.PlaylistException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    //public String addUser();
    Users register(Users user) throws UserException;
    List<Users> getAllUser();




    Users updateUser(String username, String password, String role, String email) throws CurrentUserException;
    Users readUser(String username);

    Users deleteUser(String username, Integer user_id) throws CurrentUserException, UserException;


    String verify(Users user);


}
