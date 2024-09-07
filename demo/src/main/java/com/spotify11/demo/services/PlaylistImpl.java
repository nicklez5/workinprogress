package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Playlist addSong(Integer song_id,String uuId, Integer playlist_id) throws CurrentUserException, SongException, UserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Playlist playlist1 = playlistRepo.findById(playlist_id).get();
                Song song1 = user1.getLibrary().getSongs().get(song_id);
                if(song1 != null && playlist1 != null) {
                    playlist1.getSongs().add(song1);
                    playlistRepo.save(playlist1);
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

    @Transactional
    public Playlist removeSong(Integer song_id, String uuId,Integer playlist_id) throws SongException, UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Playlist playlist1 = null;
                playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                Song song1 = user1.getLibrary().getSongs().get(song_id);
                if (song1 != null && playlist1 != null) {
                    playlist1.getSongs().remove(song1);
                    playlistRepo.save(playlist1);
                    return playlist1;
                } else {
                    throw new SongException("Song id: " + song_id + " does not exist or playlist id: " + playlist_id + " does not exist");
                }
            } else {
                throw new UserException("User id: " + uuId + " does not exist or not logged in");
            }
        } else {
            throw new CurrentUserException("User id: " + uuId + " does not exist or not logged in");

        }
    }
    @Override
    public Playlist readPlaylist(String uuId, Integer playlist_id) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                return user1.getPlaylist(playlist_id);

            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }


    @Transactional
    public Playlist deletePlaylist(String uuId,String name) throws PlaylistException, UserException,CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                List<Playlist> playlist1 = user1.getPlaylists();
                for (Playlist playlist : playlist1) {
                    if(playlist.getName().equals(name)){
                        playlistRepo.delete(playlist);
                        playlistRepo.save(playlist);
                        user1.getPlaylists().remove(playlist);
                        userRepo.save(user1);
                        return playlist;
                    }
                }

            }else{
                throw new UserException("User does not exist");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }
        return null;
    }

    @Transactional
    public Playlist createPlaylist(String uuId, String name) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSession = sessionRepo.findByUuId(uuId);
        if (currentUserSession.isPresent()) {
            CurrentUserSession userSession = currentUserSession.get();
            Optional<Users> optionalUser = userRepo.findById(userSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Playlist playlist1 = new Playlist();
                playlist1.setName(name);
                playlist1.setId((int)playlistRepo.count()+1);
                playlist1.setSongs(new ArrayList<>());
                user1.addPlaylist(playlist1);
                playlistRepo.save(playlist1);
                userRepo.save(user1);
                return playlist1;
            }else{
                throw new UserException("User is not present");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }

    }

    @Transactional
    public Playlist renamePlaylist(String uuId,Integer playlist_id, String playlist_name) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Playlist playlist1 = playlistRepo.findById(playlist_id).get();
                playlist1.setName(playlist_name);
                user1.getPlaylist(playlist_id).setName(playlist_name);
                playlistRepo.save(playlist1);
                userRepo.save(user1);
                return user1.getPlaylist(playlist_id);
                //return user1.getPlaylist().get(Integer.parseInt(id)).toString() + " has been renamed ";
            }else{
                throw new UserException("User is not present");
            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }

    }

    @Transactional
    public Playlist clearPlaylist(String uuId, Integer playlist_id) throws UserException, CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);
        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Playlist playlist1 = user1.getPlaylist(playlist_id);
                user1.getPlaylist(playlist_id).removeAllSongs();
                playlist1.getSongs().clear();
                playlistRepo.delete(playlist1);
                playlistRepo.save(playlist1);
                userRepo.save(user1);
                return playlist1;
            } else {
                throw new UserException("User is not present");
            }

        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }
    @Override
    public Playlist getPlaylist(String uuId, Integer playlist_id) throws UserException,CurrentUserException {
        Optional<CurrentUserSession> currentUserSessionOptional = sessionRepo.findByUuId(uuId);

        if (currentUserSessionOptional.isPresent()) {
            CurrentUserSession currentUserSession = currentUserSessionOptional.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                return user1.getPlaylist(playlist_id);
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
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                return user1.getPlaylists();
            }else{
                throw new UserException("User does not exist");

            }
        }else{
            throw new CurrentUserException("User is not logged in");
        }
    }
    
}
