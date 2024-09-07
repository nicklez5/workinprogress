package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;

import java.util.List;

import com.spotify11.demo.exception.PlaylistException;

public interface UserService {
    //public String addUser();
    Users addUser(Users user) throws UserException;
    List<Users> getAllUser();
    Users updateUser(Users user, String uuId) throws CurrentUserException;
    Users readUser(String uuId) throws CurrentUserException;
    Users deleteUser(String uuId, Integer id) throws CurrentUserException, UserException;
    CurrentUserSession logIn(Login logIn) throws CurrentUserException;
    String logOut(String uuId) throws CurrentUserException;
    Users assignPlaylistToUser(String uuId, Integer playlist_id) throws PlaylistException;
    String verify(Users user);
}
