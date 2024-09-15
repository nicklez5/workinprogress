package com.spotify11.demo.services;


import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public interface SongService {


    UploadFileResponse createSong(String title, String artist, MultipartFile file,String email) throws Exception;
    Resource loadFileAsResource(String fileName) throws FileNotFoundException, FileNotFoundException, Exception;
    void loadAllSongs();


    Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String email) throws SongException, FileNotFoundException, UserException;

    String getSong(String title) throws SongException;
    List<Song> getAllSongs() throws UserException, SongException;

}
