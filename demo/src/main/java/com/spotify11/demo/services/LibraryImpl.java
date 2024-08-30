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
import com.spotify11.demo.exception.LibraryException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
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
        User user1 = this.getUser(uuId);
        Library tmp_lib = user1.getLibrary();
        if(tmp_lib != null && user1 != null ){
            tmp_lib.addSong(song1);
            Library library2 = libraryRepo.save(tmp_lib);
            return library2;
        }
        else if(user1 == null && song1 != null){
            throw new CurrentUserException("User is null");
        }
        else if(song1 == null && user1 != null){
            throw new SongException("Song is null");
        }
        return null;

        //throw new UnsupportedOperationException("Unimplemented method 'addSong'");
    }

    @Override
    public Library updateSongs(Library library, String uuId) throws CurrentUserException {
        User user1 = this.getUser(uuId);
        if(user1 != null && user1.getLibrary() != null){
            if(user1.getRole().equals("ADMIN") || user1.getRole().equals("USER")){
                if(user1.getId() == user1.getId()){
                    Library library2 = libraryRepo.save(library);
                    return library2;
                }else{
                    throw new CurrentUserException("User Id is not the same");
                }
            }else{
                throw new CurrentUserException("You dont have access");
            }
        }else{
            throw new CurrentUserException("User is null");
        }
        
        
    }

    @Override
    public List<Song> getAllSongs(String uuId) throws CurrentUserException {
        User user1 = this.getUser(uuId);
        if(user1 != null){
            List<Song> songs1 = user1.getLibrary().getSongs();
            return songs1;
        }else{
            throw new CurrentUserException("User is null");
        }
        
    }

    @Override
    public Library deleteSong(String uuId, Song song1) throws CurrentUserException {
        User user1 = this.getUser(uuId);
        if(user1 != null){
            if(user1.getRole().equals("ADMIN")){
                Library library1 = user1.getLibrary();
                library1.removeSong(song1);
                libraryRepo.save(library1);
                userRepo.save(user1);
                return library1;
            }else{
                throw new CurrentUserException("You dont have access");
            }
        }else{
            throw new CurrentUserException("The user is null");
        }
        
        
    }
    // private Library getLibrary(String uuId){
    //     Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
    //     if(optionalSession.isPresent()){
    //         CurrentUserSession session = optionalSession.get();
    //         Optional<Library> optionalLibrary = libraryRepo.findById(session.getUserId())
    //         return optionalLibrary.get();
    //     }
    //     return null;
    // }
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
