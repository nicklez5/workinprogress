package com.spotify11.demo.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.SongRepo;

@Service
public class SongImpl implements SongService {

    @Autowired
    private SongRepo repo;

    


    @Override
    public Song createSong(Integer song_id, String title, String artist, File song_file) throws FileNotFoundException {
        Song song1 = new Song();
        song1.setTitle(title);
        song1.setSong_id(song_id);
        song1.setArtist(artist);
        if(song_file != null) {
            song1.setSong_file(song_file);
            repo.save(song1);
            return song1;
        }else{
            throw new FileNotFoundException("File not found");
        }
        

    }

    @Override
    public String readSong(Song song) throws SongException {
        if(song == null) {
            throw new SongException("Song is null");
        }else{
            return song.toString();
        }
    }

    @Override
    public Song updateSong(String title, String artist, Duration duration, File song_file, Integer song_id) throws FileNotFoundException {
        Song song1 = repo.getReferenceById(String.valueOf(song_id));
        song1.setArtist(artist);
        song1.setTitle(title);

        if(song_file == null){
            throw new FileNotFoundException("Song file not found");
        }else{
            song1.setSong_file(song_file);
            repo.save(song1);
            return song1;
        }
    }

    @Override
    public void deleteSong(Song song) throws SongException {
        if(song != null){
            repo.delete(song);
            
        }else{
            throw new SongException("Song is null");
        }
        
    }

    @Override
    public List<Song> getSongs(){
        // TODO Auto-generated method stub
        List<Song> xyz = repo.findAll();
        return xyz;
    }

    @Override
    public Song getSong(int id) throws SongException {
        Optional<Song> xyz = repo.findById(String.valueOf(id));
        if(xyz.isPresent()){
            return xyz.get();
        }else{
            throw new SongException("Song is not found");
        }
    }

    @Override
    public Song getSong(String title) throws SongException {
        Optional<Song> xyz = repo.findByTitle(title);
        if(xyz.isPresent()){
            return xyz.get();
        }else{
            throw new SongException("Song is not found");
        }
        // TODO Auto-generated method stub
        
    }

   

    
    
}
