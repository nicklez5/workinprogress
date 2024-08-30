package com.spotify11.demo.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartException;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;

public class SongImpl implements SongService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SongRepo songRepo;
    @Override
    public Song createSong(int song_id, String title, String artist, File song_file, String uuId) throws FileNotFoundException {
        User user1 = this.getUser(uuId);
        Song song1 = new Song();
        song1.setTitle(title);
        song1.setSong_id(song_id);
        song1.setArtist(artist);
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSong'");
    }

    @Override
    public Song readSong(Song song) throws SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readSong'");
    }

    @Override
    public Song updateSong(Song song) throws CurrentUserException, SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSong'");
    }

    @Override
    public void deleteSong(Song song) throws CurrentUserException, SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSong'");
    }

    @Override
    public List<Song> getSongs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSongs'");
    }

    @Override
    public Song getSong(int id) throws CurrentUserException, SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSong'");
    }

    @Override
    public Song getSong(String title) throws CurrentUserException, SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSong'");
    }

    @Override
    public Song getSong(String title, String artist) throws CurrentUserException, SongException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSong'");
    }

    @Override
    public Song downloadSong(File file) throws CurrentUserException, MultipartException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'downloadSong'");
    }

   
    private User getUser(String uuId){
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if(optionalSession.isPresent()){
            CurrentUserSession session = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(session.getUserId());
            return optionalUser.get();
        }
        return null;
    }

    
    
}
