package com.spotify11.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Library;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.LibraryRepo;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
@Service
public class LibraryImpl implements LibraryService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LibraryRepo libraryRepo;

    @Autowired
    private SessionRepo sessionRepo;
    
    @Override
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

    @Override
    public Library updateSongs(Library library, String uuId) throws CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if(currentUserSession.isPresent()){
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                if(user.getLibrary() != null){
                    if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")){
                        if(user.getId() == user.getId()){
                            user.setLibrary(library);
                            userRepo.save(user);
                            return user.getLibrary();
                            
                        }else{
                            throw new CurrentUserException("User Id is not the same");
                        }
                    }else{
                        throw new CurrentUserException("You dont have access");
                    }
                }else{
                    throw new CurrentUserException("User is null");
                }
            }else{
                throw new CurrentUserException("User is not present");
            }
        }else{
            throw new CurrentUserException("User is null");
        }

    }


    @Override
    public List<Song> getAllSongs(String uuId) throws CurrentUserException {
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

    @Override
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


	
    
    
}
