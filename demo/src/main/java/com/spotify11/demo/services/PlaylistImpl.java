package com.spotify11.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.repo.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;

@Service
public class PlaylistImpl implements PlaylistService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SongRepo songRepo;

    @Autowired
    SessionRepo sessionRepo;

    @Override
    public Playlist addSong(Song song, String uuId) throws SongException, UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                if(song != null){
                    user1.getPlaylist().addSongs(song);
                    userRepo.save(user1);
                    return user1.getPlaylist();
                }else{
                    throw new SongException("Song does not exist");
                }

            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }
    }

    @Override
    public Playlist removeSong(Song song, String uuId) throws SongException, UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                if(song != null){
                    user1.getPlaylist().deleteSongs(song);
                    userRepo.save(user1);
                    return user1.getPlaylist();
                }else{
                    throw new SongException("Song does not exist");
                }
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

    }

    @Override
    public Playlist readPlayist(String uuId) throws UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                return user1.getPlaylist();
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

        
        
    }

    public Integer randomNumber(){
        Random rng = new Random();
        int whatever = 1 + rng.nextInt(1000);
        return whatever;
    }

    @Override
    public Playlist deletePlaylist(String uuId) throws PlaylistException, UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                Playlist playlist1 = user1.getPlaylist();
                if(playlist1 != null){
                    List<Song> songs = new ArrayList<>();
                    Playlist playlist2 = new Playlist(this.randomNumber(),"",songs);
                    user1.setPlaylist(playlist2);
                    userRepo.save(user1);
                    return user1.getPlaylist();
                }else{
                    throw new PlaylistException("Playlist does not exist");
                }
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

    }

    @Override
    public Playlist createPlaylist(String uuId, String name) throws UserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if (currentUserSession.isPresent()) {
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if (optionalUser.isPresent()) {
                List<Song> songs = new ArrayList<>();
                User user1 = optionalUser.get();
                Playlist playlist1 = new Playlist(this.randomNumber(),name,songs);
                user1.setPlaylist(playlist1);
                userRepo.save(user1);
                return user1.getPlaylist();
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

    }

    @Override
    public void renamePlaylist(String uuId, String name) throws UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                user1.getPlaylist().setPlaylist_name(name);
                userRepo.save(user1);
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

    }

    @Override
    public List<Song> getAllSongs(String uuId) throws UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                return user1.getPlaylist().getSongs();
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new UserException("User does not exist");
        }

    }
    
}
