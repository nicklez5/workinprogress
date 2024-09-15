package com.spotify11.demo.services;

import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;


import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.repo.LibraryRepo;

import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Set;


@Service
public class LibraryImpl implements LibraryService {


    private final LibraryRepo libRepo;

    public String basePath = "C:/Users/jesus/Downloads/demo(13)/demo/src/main/resources/static/";
    @Autowired
    public LibraryImpl(LibraryRepo libRepo) {
        this.libRepo = libRepo;

    }

    public String addSong(Song song,Integer library_id) throws LibraryException {
        if(this.libRepo.findById(library_id).isPresent()) {
            Set<Song> xyz = this.libRepo.findById(library_id).get().getSongs();
            xyz.add(song);
            return song.toString() + "has been added to library";
        } else{
            throw new LibraryException("No library found");
        }

    }

    @Transactional
    public String deleteSong(Song song1,Integer library_id) throws LibraryException {
        if(this.libRepo.findById(library_id).isPresent()){
            Set<Song> xyz = this.libRepo.findById(library_id).get().getSongs();
            xyz.remove(song1);
            return song1.toString() + " has been deleted from library";
        }else{
            throw new LibraryException("No library found");
        }

    }


    public Set<Song> getLibrary(Integer library_id) throws LibraryException {
        if(this.libRepo.findAll().isEmpty()){
            throw new LibraryException("No library found");

        }else{
            Set<Song> xyz123 = this.libRepo.findById(library_id).get().getSongs();
            return xyz123;
        }
    }

    public Library clearLibrary(Integer library_id) throws LibraryException {
        if(this.libRepo.findAll().isEmpty()){
            throw new LibraryException("No library found");
        }else{
            this.libRepo.findById(library_id).get().getSongs().clear();
            return libRepo.findById(library_id).get();
        }

    }


}

