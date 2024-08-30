package com.spotify11.demo.services;

import java.util.List;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.SongException;

public interface PlaylistService {
    public Playlist addSong(Song song) throws SongException;
    public Playlist removeSong(Song song);
    public Playlist updatePlaylist(Playlist playlist, String uuId) throws SongException;
    public Playlist readPlayist(String uuId) throws SongException;
    public Playlist deletePlaylist(String uuId, Integer id) throws SongException;
    public List<Song> getAllSongs(Playlist playlist);

}
