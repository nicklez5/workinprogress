package com.spotify11.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.repo.UserRepo;

@Service
public class PlaylistImpl implements PlaylistService {

    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;
    private final PlaylistRepo playlistRepo;
    
    public PlaylistImpl(UserRepo userRepo, SessionRepo sessionRepo, PlaylistRepo playlistRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.playlistRepo = playlistRepo;
    }

    @Override
    public Playlist addSong(Song song, String uuId, Integer id) throws CurrentUserException, SongException, UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                Playlist playlist1 = playlistRepo.findById(id).orElse(null);
                if(song != null){

                    user1.getPlaylist().get(id).addSongs(song);
                    userRepo.save(user1);
                    return playlist1;

                }else{
                    throw new SongException("Song does not exist");
                }

            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new CurrentUserException("User id: " + uuId + " does not exist or not logged in");
        }

    }

    @Override
    public Playlist removeSong(Song song, String uuId,Integer id) throws SongException, UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                Playlist playlist1 = playlistRepo.findById(id).get();
                if(song != null){
                    user1.getPlaylist().get(id).getSongs().remove(song);
                    userRepo.save(user1);
                    return playlist1;
                }else{
                    throw new SongException("Song id: " + id + " does not exist");
                }
            }else{
                throw new UserException("User uuId: " + uuId + " is not present");
            }
        }else{
            throw new CurrentUserException("User uuId:" + uuId + " is not present or does not exist");
        }

    }
    @Override
    public Playlist readPlaylist(String uuId, Integer id) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                return user1.getPlaylist(id);

            }else{
                throw new UserException("User: " + id +  "does not exist");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }




    @Override
    public String deletePlaylist(String uuId,Integer id) throws PlaylistException, UserException,CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                Playlist playlist1 = user1.getPlaylist(id);
                if(playlist1 != null){
                    playlistRepo.delete(playlist1);
                    return "Playlist id:" + id + " has been removed";
                }else{
                    throw new PlaylistException("Playlist does not exist");
                }
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }

    }

    @Override
    public Playlist createPlaylist(String uuId, String name) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if (currentUserSession.isPresent()) {
            CurrentUserSession userSession = currentUserSession.get();
            Optional<User> optionalUser = userRepo.findById(userSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                Playlist playlist1 = new Playlist((int)this.playlistRepo.count() ,name,new ArrayList<>());
                user1.addPlaylist(playlist1);

                userRepo.save(user1);
                return playlist1;
            }else{
                throw new UserException("User is not present");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }

    }

    @Override
    public Playlist renamePlaylist(String uuId,Integer id, String name) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                user1.getPlaylist().get(id).setPlaylist_name(name);
                userRepo.save(user1);

                return user1.getPlaylist(id);
                //return user1.getPlaylist().get(Integer.parseInt(id)).toString() + " has been renamed ";
            }else{
                throw new UserException("User is not present");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }

    }

    @Override
    public String clearPlaylist(String uuId, Integer id) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                user1.getPlaylist().clear();
                userRepo.save(user1);
                return "Playlist: " + id + " has been cleared";
            } else {
                throw new UserException("User is not present");
            }

        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }
    @Override
    public Playlist getPlaylist(String uuId, Integer id) throws UserException,CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);

        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user1 = optionalUser.get();
                return user1.getPlaylist().get(id);
            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }
    @Override
    public List<Playlist> getAllPlaylists(String uuId) throws UserException, CurrentUserException{
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
            throw new CurrentUserException("User is not logged in");
        }
    }
    
}
