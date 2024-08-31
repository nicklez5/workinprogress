package com.spotify11.demo.services;

import java.util.List;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

public interface PlaylistService {
    public Playlist addSong(Song song,String uuId) throws SongException, UserException;
    public Playlist removeSong(Song song,String uuId) throws SongException, UserException;
    public Playlist readPlayist(String uuId) throws UserException;
    public Playlist deletePlaylist(String uuId) throws PlaylistException, UserException;
    public Playlist createPlaylist(String uuId, String name) throws UserException;
    public void renamePlaylist(String uuId, String name) throws UserException;
    public List<Song> getAllSongs(String uuId) throws UserException;


}
