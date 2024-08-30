package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.multipart.MultipartException;

public interface SongService {
    public Song createSong(int song_id,String title, String artist, File song_file, String uuId) throws FileNotFoundException;
    public Song readSong(Song song) throws SongException;
    public Song updateSong(Song song) throws CurrentUserException, SongException;
    public void deleteSong(Song song) throws CurrentUserException, SongException;
    public List<Song> getSongs();
    public Song getSong(int id) throws CurrentUserException,SongException;
    public Song getSong(String title) throws CurrentUserException,SongException;
    public Song getSong(String title, String artist) throws CurrentUserException,SongException;
    public Song downloadSong(File file) throws CurrentUserException,MultipartException;




}
