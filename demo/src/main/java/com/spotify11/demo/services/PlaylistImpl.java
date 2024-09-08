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
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistImpl implements PlaylistService {



    private final UserRepo userRepo;
    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;
    public PlaylistImpl(UserRepo userRepo, PlaylistRepo playlistRepo, SongRepo songRepo) {
        this.userRepo = userRepo;
        this.playlistRepo = playlistRepo;

        this.songRepo = songRepo;
    }

    @Transactional
    public Playlist addSong(Integer song_id,String username, Integer playlist_id) throws SongException, UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                if(playlist1 != null) {
                    Song song1 = songRepo.findById(song_id).orElse(null);
                    if(song1 != null) {
                        playlist1.getSongs().add(song1);
                        playlistRepo.save(playlist1);
                        return playlist1;
                    }else{
                        throw new SongException("Song does not exist");
                    }
                }else{
                    throw new PlaylistException("Playlist id:" + playlist_id + " could not be found");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }


    }

    @Transactional
    public Playlist removeSong(Integer song_id, String username,Integer playlist_id) throws SongException, UserException,PlaylistException {
        Users user = userRepo.findByUsername(username);
        if (user != null) {
            Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
            if (playlist1 != null) {
                Song song1 = songRepo.findById(song_id).orElse(null);
                if (song1 != null) {
                    playlist1.getSongs().remove(song1);
                    playlistRepo.save(playlist1);
                    return playlist1;
                } else {
                    throw new SongException("Song does not exist");
                }
            } else {
                throw new PlaylistException("Playlist id:" + playlist_id + " could not be found");
            }

        }else{
            throw new UserException("User: " + username + " not found");
        }
    }
    @Override
    public Playlist readPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                if(playlist1 != null) {
                    return playlist1;
                }else {
                    throw new PlaylistException("Playlist Id: " + playlist_id + " could not be found");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }

    }


    @Transactional
    public Playlist deletePlaylist(String username,String playlist_name) throws PlaylistException, UserException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Set<Playlist> playlist1 = user.getPlaylists();
                if(playlist1 != null) {
                    for (Playlist playlist : playlist1) {
                        if(playlist.getName().equals(playlist_name)) {
                            playlistRepo.delete(playlist);
                            user.getPlaylists().remove(playlist);
                            userRepo.save(user);
                            return playlist;
                        }else{
                            throw new PlaylistException("Playlist name:" + playlist_name + " could not be found");
                        }
                    }
                }else{
                    throw new PlaylistException("User: " + username + "Playlist is null");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }
          return null;
    }

    @Transactional
    public Playlist createPlaylist(String username, String playlist_name) throws UserException{
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = new Playlist();
                playlist1.setName(playlist_name);
                playlist1.setId((int)playlistRepo.count()+1);
                playlistRepo.save(playlist1);
                user.addPlaylist(playlist1);
                userRepo.save(user);
                return playlist1;
            }else{
                throw new UserException("User: " + username + " not found");
            }



    }

    @Transactional
    public Playlist renamePlaylist(String username,Integer playlist_id, String playlist_name) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                if(playlist1 != null) {
                    playlist1.setName(playlist_name);
                    user.getPlaylist(playlist_id).setName(playlist_name);
                    userRepo.save(user);
                    playlistRepo.save(playlist1);
                    return playlist1;
                }else{
                    throw new PlaylistException("Playlist id: " + playlist_id + " is null");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }



    }

    @Transactional
    public Playlist clearPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                if(playlist1 != null) {
                    playlist1.getSongs().clear();
                    playlistRepo.save(playlist1);
                    userRepo.save(user);
                    return playlist1;
                }else{
                    throw new PlaylistException("Playlist id: " + playlist_id + " is null");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }



    }
    @Override
    public Playlist getPlaylist(String username, Integer playlist_id) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = playlistRepo.findById(playlist_id).orElse(null);
                if (playlist1 != null) {
                    return playlist1;
                } else {
                    throw new PlaylistException("Playlist id: " + playlist_id + " is null");
                }
            }else{
                    throw new UserException("User: " + username + " not found");
            }

    }
    @Override
    public Set<Playlist> getAllPlaylists(String username) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Set<Playlist> playlists = user.getPlaylists();
                if (playlists != null) {
                    return playlists;
                } else {
                    throw new PlaylistException("Playlists null");
                }
            }else {
                throw new UserException("User: " + username + " not found");
            }

    }
    
}
