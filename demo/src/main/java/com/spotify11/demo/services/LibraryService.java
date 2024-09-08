package com.spotify11.demo.services;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.*;

import java.util.List;

public interface LibraryService {

    Library addSong(Song song1, String username) throws SongException;
    Library deleteSong(Song song1, String username) throws SongException;
    Library getLibrary(String username) throws LibraryException;
    Library clearLibrary(String username) throws LibraryException;


}
