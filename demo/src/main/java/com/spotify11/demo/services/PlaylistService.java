package com.spotify11.demo.services;


import com.spotify11.demo.entity.Playlist;


import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import org.springframework.http.ResponseEntity;

public interface PlaylistService {

    Playlist addSong(Integer song_id,String email) throws SongException, UserException, PlaylistException;
    Playlist removeSong(Integer song_id,String email) throws SongException, UserException, PlaylistException;
    String getSongs(String email);
    Playlist readPlaylist(String email) throws UserException, PlaylistException;
    Playlist deletePlaylist(String email) throws PlaylistException, UserException;
    Playlist createPlaylist(String email, String playlist_name) throws UserException;
    String renamePlaylist(String email, String playlist_name) throws UserException,  PlaylistException;
    String clearPlaylist(String email) throws UserException, PlaylistException;
    String getPlaylist(String email) throws UserException, PlaylistException;




}
