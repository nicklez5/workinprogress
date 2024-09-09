package com.spotify11.demo.services;


import com.spotify11.demo.entity.Playlist;


import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

public interface PlaylistService {

    Playlist addSong(Integer song_id,String username) throws SongException, UserException, PlaylistException;
    Playlist removeSong(Integer song_id,String username) throws SongException, UserException, PlaylistException;
    String getSongs(String username);
    Playlist readPlaylist(String username) throws UserException, PlaylistException;
    String deletePlaylist(String username) throws PlaylistException, UserException;
    Playlist createPlaylist(String username, String playlist_name) throws UserException;
    String renamePlaylist(String username, String playlist_name) throws UserException,  PlaylistException;
    String clearPlaylist(String username) throws UserException, PlaylistException;

    String getPlaylist(String username) throws UserException, PlaylistException;




}
