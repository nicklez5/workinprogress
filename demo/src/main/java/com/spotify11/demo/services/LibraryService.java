package com.spotify11.demo.services;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.*;

import java.util.List;

public interface LibraryService {

    Library addSong(Song song, String uuId) throws LibraryException, CurrentUserException, SongException;
    Library deleteSong(String uuId, Song song1) throws CurrentUserException;
    Library getLibrary(String uuId) throws CurrentUserException;
    Library clearLibrary(String uuId) throws CurrentUserException;


}
