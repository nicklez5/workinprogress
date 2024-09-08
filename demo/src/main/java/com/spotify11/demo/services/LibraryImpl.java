package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.LibraryRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LibraryImpl implements LibraryService {


    private final UserRepo userRepo;

    private final LibraryRepo libraryRepo;


    
    public LibraryImpl(UserRepo userRepo, LibraryRepo libraryRepo) throws CurrentUserException {
        this.userRepo = userRepo;
        this.libraryRepo = libraryRepo;
    }


    @Transactional
    public Library addSong(Song song1,String username) throws SongException{
        Users user = userRepo.findByUsername(username);
        if (song1 != null) {
            user.getLibrary().addSong(song1);
            userRepo.save(user);
            return user.getLibrary();
        } else {
            throw new SongException("Song does not exist");
        }
    }

    @Transactional
    public Library deleteSong(Song song1, String username) throws SongException {
        Users user = userRepo.findByUsername(username);
        if (song1 != null) {
            user.getLibrary().removeSong(song1);
            userRepo.save(user);
            return user.getLibrary();
        }else{
            throw new SongException("song does not exist");
        }

    }



    public Library getLibrary(String username) throws LibraryException {
        Users user = userRepo.findByUsername(username);
        if(user.getLibrary() == null){
            throw new LibraryException("Library is null");
//            Library library = new Library();
//            user.setLibrary(library);
//            libraryRepo.save(library);
//            userRepo.save(user);
//            return library;
        }else{
            return user.getLibrary();
        }
    }

    @Transactional
    public Library clearLibrary(String username) throws LibraryException {
        Users user = userRepo.findByUsername(username);
            if(user.getLibrary() == null) {
                throw new LibraryException("Library is null");
            }else {
                Library library = new Library();
                user.setLibrary(library);
                libraryRepo.save(library);
                userRepo.save(user);
                return library;
            }
    }

}
