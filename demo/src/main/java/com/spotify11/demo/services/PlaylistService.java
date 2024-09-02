package com.spotify11.demo.services;

import java.util.List;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

public interface PlaylistService {

    Playlist addSong(Song song,String uuId, Integer id) throws SongException, UserException, CurrentUserException;
    Playlist removeSong(Song song,String uuId, Integer id) throws SongException, UserException, CurrentUserException;
    Playlist readPlaylist(String uuId, Integer id) throws UserException, CurrentUserException;
    String deletePlaylist(String uuId,Integer id) throws PlaylistException, UserException, CurrentUserException;
    Playlist createPlaylist(String uuId, String name) throws UserException, CurrentUserException;
    Playlist renamePlaylist(String uuId, Integer id, String name) throws UserException, CurrentUserException;
    String clearPlaylist(String uuId, Integer id) throws UserException, CurrentUserException;

    Playlist getPlaylist(String uuId, Integer id) throws UserException,CurrentUserException;
    List<Playlist> getAllPlaylists(String uuId) throws UserException, CurrentUserException;

    //public List<Song> getAllSongs(String uuId,String id) throws UserException;

}
