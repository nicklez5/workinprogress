package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import java.util.List;

import com.spotify11.demo.exception.PlaylistException;

public interface UserService {
    //public String addUser();
    User addUser(User user) throws UserException;
    List<User> getAllUser();
    User updateUser(User user, String uuId) throws CurrentUserException;
    User readUser(String uuId) throws CurrentUserException;
    User deleteUser(String uuId, Integer id) throws CurrentUserException, UserException;
    CurrentUserSession logIn(Login logIn) throws CurrentUserException;
    String logOut(String uuId) throws CurrentUserException;


}
