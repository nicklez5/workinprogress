package com.spotify11.demo.services;

import java.util.List;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

public interface PlaylistService {

    Playlist addSong(Integer song_id,String uuId, Integer playlist_id) throws SongException, UserException, CurrentUserException;
    Playlist removeSong(Integer song_id,String uuId, Integer playlist_id) throws SongException, UserException, CurrentUserException;
    Playlist readPlaylist(String uuId, Integer playlist_id) throws UserException, CurrentUserException;
    Playlist deletePlaylist(String uuId,String name) throws PlaylistException, UserException, CurrentUserException;
    Playlist createPlaylist(String uuId, String name) throws UserException, CurrentUserException;
    Playlist renamePlaylist(String uuId, Integer playlist_id, String playlist_name) throws UserException, CurrentUserException;
    Playlist clearPlaylist(String uuId, Integer playlist_id) throws UserException, CurrentUserException;

    Playlist getPlaylist(String uuId, Integer playlist_id) throws UserException,CurrentUserException;
    List<Playlist> getAllPlaylists(String uuId) throws UserException, CurrentUserException;

    //public List<Song> getAllSongs(String uuId,String id) throws UserException;

}
