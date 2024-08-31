package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Login;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;

import java.util.List;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.exception.PlaylistException;

public interface UserService {
    //public String addUser();
    public User addUser(User user) throws UserException;
    public List<User> getAllUser();
    public User updateUser(User user, String uuId) throws CurrentUserException;
    public User readUser(String uuId) throws CurrentUserException;
    public User deleteUser(String uuId, Integer id) throws CurrentUserException, UserException;
    public CurrentUserSession logIn(Login logIn) throws CurrentUserException;
    public String logOut(String uuId) throws CurrentUserException;
    public User setPlaylist(Playlist playlist1, Integer uuId) throws CurrentUserException, PlaylistException;
}
