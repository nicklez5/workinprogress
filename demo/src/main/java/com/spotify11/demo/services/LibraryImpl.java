package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.LibraryRepo;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LibraryImpl implements LibraryService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final LibraryRepo libraryRepo;
    @Autowired
    private final SessionRepo sessionRepo;

    
    public LibraryImpl(UserRepo userRepo, LibraryRepo libraryRepo, SessionRepo sessionRepo) {
        this.userRepo = userRepo;
        this.libraryRepo = libraryRepo;
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public Library addSong(Song song1, String uuId) throws SongException, CurrentUserException {

        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        //User user1 = sessionRepo.findByUuId(uuId).
        if (currentUserSession.isPresent()) {
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if(song1 != null){
                    user.getLibrary().addSong(song1);
                    userRepo.save(user);
                    return user.getLibrary();
                }
                else {
                    throw new SongException("Song is null");
                }
            }else{
                throw new CurrentUserException("User is not found");
            }
        }else{
            throw new CurrentUserException("User is null");
        }

       
    }





    @Transactional
    public Library deleteSong(String uuId, Song song1) throws CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if(currentUserSession.isPresent()){
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                if(user.getLibrary() != null){
                    user.getLibrary().removeSong(song1);
                    userRepo.save(user);
                    return user.getLibrary();
                }else{
                    throw new CurrentUserException("User is not found");
                }
            }else{
                throw new CurrentUserException("User is null");
            }
        }else{
            throw new CurrentUserException("User is null");
        }
    }



    public List<Song> getLibrary(String uuId) throws CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if(currentUserSession.isPresent()){
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                return user.getLibrary().getSongs();
            }else{
                throw new CurrentUserException("User is not found");
            }
        }else{
            throw new CurrentUserException("User is null");
        }
    }

    @Transactional
    public Library clearLibrary(String uuId) throws CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if(currentUserSession.isPresent()){
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                Library library = new Library((int) this.libraryRepo.count());
                user.setLibrary(library);
                userRepo.save(user);
                return user.getLibrary();

            }else{
                throw new CurrentUserException("User is not found");
            }
        }else{
            throw new CurrentUserException("User is null");
        }
    }

}
