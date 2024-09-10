package com.spotify11.demo.services;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public Playlist addSong(Integer song_id,String username) throws SongException, UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
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
                    throw new PlaylistException("Playlist could not be found");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }


    }

    @Transactional
    public Playlist removeSong(Integer song_id, String username) throws SongException, UserException,PlaylistException {
        Users user = userRepo.findByUsername(username);
        if (user != null) {
            Playlist playlist1 = user.getPlaylist();
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
                throw new PlaylistException("Playlist could not be found");
            }

        }else{
            throw new UserException("User: " + username + " not found");
        }
    }
    @Override
    public Playlist readPlaylist(String username) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null) {
                    return playlist1;
                }else {
                    throw new PlaylistException("Playlist could not be found");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }

    }


    @Transactional
    public String deletePlaylist(String username) throws PlaylistException, UserException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null) {
                    playlist1.getSongs().clear();
                    playlistRepo.save(playlist1);
                    playlist1 = new Playlist();
                    user.setPlaylist(playlist1);
                    userRepo.save(user);
                    return "Your playlist has been deleted";
                }else{
                    throw new PlaylistException("User: " + username + "Playlist is null");
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }

    }

    @Transactional
    public Playlist createPlaylist(String username, String playlist_name) throws UserException{
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = new Playlist();
                playlist1.setName(playlist_name);
                playlistRepo.save(playlist1);
                user.setPlaylist(playlist1);
                userRepo.save(user);
                return playlist1;
            }else{
                throw new UserException("User: " + username + " not found");
            }



    }

    @Transactional
    public String renamePlaylist(String username, String playlist_name) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                user.getPlaylist().setName(playlist_name);
                userRepo.save(user);
                return "Your playlist has been renamed";
            }else{
                throw new UserException("User: " + username + " not found");
            }



    }

    @Transactional
    public String clearPlaylist(String username) throws UserException, PlaylistException {
            Users user = userRepo.findByUsername(username);
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null) {
                    playlist1.getSongs().clear();
                    playlistRepo.save(playlist1);

                    return "Your playlist has been cleared";
                }
            }else{
                throw new UserException("User: " + username + " not found");
            }

        return null;

    }
    @Override
    public String getPlaylist(String username) throws UserException, PlaylistException {
        Users user1 = userRepo.findByUsername(username);
        if(user1 != null){
            return user1.getPlaylist().toString();
        }else{
            throw new UserException("User: " + username + " not found");
        }

    }
    @Override
    public String getSongs(String username){
        Users user1 = userRepo.findByUsername(username);
        if(user1 != null){
            return user1.getPlaylist().getSongs().toString();
        }else{
            return "User: " + username + " not found";
        }

    }

    
}
