package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartException;

public interface SongService {
    public Song createSong(Integer song_id,String title, String artist, File song_file) throws FileNotFoundException;
    public String readSong(Song song) throws SongException;
    public Song updateSong(String title, String artist, Duration duration, File songFile, Integer song_id) throws SongException, FileNotFoundException;
    public void deleteSong(Song song) throws SongException;
    public List<Song> getSongs() throws SongException;
    public Song getSong(int id) throws SongException;
    public Song getSong(String title) throws SongException;
   





}
