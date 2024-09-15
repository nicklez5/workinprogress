package com.spotify11.demo.services;


import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;


import java.util.Set;

public interface PlaylistService {

    String addSong(Song song1, Integer playlist_id) throws SongException;
    String removeSong(Song song1, Integer playlist_id) throws SongException;
    Set<Song> getSongs();
    void deletePlaylist(Integer playlist_id) throws PlaylistException;
    void readPlaylist(Integer playlist_id);
    void renamePlaylist(String playlist_name, Integer playlist_id) throws PlaylistException;
    void clearPlaylist(Integer playlist_id) throws PlaylistException;
    Set<Song> getPlaylist(Integer playlist_id) throws PlaylistException;




}
