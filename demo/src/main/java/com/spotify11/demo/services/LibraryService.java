package com.spotify11.demo.services;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.*;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.util.Set;

public interface LibraryService {

    String addSong(Song song1, Integer library_id) throws LibraryException;
    String deleteSong(Song song1, Integer library_id) throws LibraryException;
    Set<Song> getLibrary(Integer library_id) throws LibraryException;
    Library clearLibrary(Integer library_id) throws LibraryException;

}
