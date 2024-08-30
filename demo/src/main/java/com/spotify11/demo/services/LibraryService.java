package com.spotify11.demo.services;

import java.util.List;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.*;
public interface LibraryService {
    public Library addSong(Song song, String uuId) throws LibraryException, CurrentUserException, SongException;
    public Library updateSongs(Library library, String uuId)  throws CurrentUserException;
    public List<Song> getAllSongs(String uuId) throws CurrentUserException;
    public Library deleteSong(String uuId, Song song1) throws CurrentUserException;
    
}
