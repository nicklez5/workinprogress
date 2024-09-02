package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface SongService {


    Song createSong(String title, String artist, MultipartFile file, String uuId) throws  UserException, Exception;
    Song readSong(String fileName, String uuId) throws UserException;
    Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String uuId) throws UserException,  SongException, FileNotFoundException, IOException;
    String addFileToLibrary(MultipartFile file) throws IOException;
    byte[] getFilefromSong(Song song1);
    byte[] downloadFileFromLibrary(String fileName) throws IOException;
    void deleteSong(Song song, String uuId) throws UserException, SongException;
    Song getSong(int id, String uuId) throws  UserException,SongException;
    Song getSong(String title, String uuId) throws  UserException,SongException;
    List<Song> getAllSongs(String uuId) throws UserException, SongException;
}
