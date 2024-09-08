package com.spotify11.demo.services;

import java.util.List;
import java.util.Set;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

public interface PlaylistService {

    Playlist addSong(Integer song_id,String username, Integer playlist_id) throws SongException, UserException, PlaylistException;
    Playlist removeSong(Integer song_id,String username, Integer playlist_id) throws SongException, UserException, PlaylistException;
    Playlist readPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException;
    Playlist deletePlaylist(String username,String playlist_name) throws PlaylistException, UserException;
    Playlist createPlaylist(String username, String playlist_name) throws UserException;
    Playlist renamePlaylist(String username, Integer playlist_id, String playlist_name) throws UserException,  PlaylistException;
    Playlist clearPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException;

    Playlist getPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException;
    Set<Playlist> getAllPlaylists(String username) throws UserException ,PlaylistException;

    //public List<Song> getAllSongs(String uuId,String id) throws UserException;

}
