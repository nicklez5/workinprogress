package com.spotify11.demo.services;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;

import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;

import com.spotify11.demo.repo.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
public class PlaylistImpl implements PlaylistService {



    private final UserRepository userRepo;
    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;

    public PlaylistImpl(UserRepository userRepo, PlaylistRepo playlistRepo, SongRepo songRepo) {
        this.userRepo = userRepo;
        this.playlistRepo = playlistRepo;
        this.songRepo = songRepo;
    }

    @Transactional
    public Playlist addSong(Integer song_id,String email) throws SongException, UserException, PlaylistException {
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null){
                    Song song1 = songRepo.findById(song_id).orElse(null);
                    if(song1 != null) {
                        playlist1.getSongs().add(song1);
                        playlistRepo.save(playlist1);
                        return playlist1;
                    }else{
                        throw new SongException("Song does not exist");
                    }
                }else{
                    throw new PlaylistException("Playlist is null");
                }

            
            }else{
                throw new UserException("User with email: " + email + " not found");
            }


    }

    @Transactional
    public Playlist removeSong(Integer song_id, String email) throws SongException, UserException, PlaylistException {
        User user = userRepo.findByEmail(email).get();
        if (user != null) {
            Playlist playlist1 = user.getPlaylist();
            if(playlist1 != null){
                Song song1 = songRepo.findById(song_id).orElse(null);
                if (song1 != null) {
                    playlist1.getSongs().remove(song1);
                    playlistRepo.save(playlist1);
                    return playlist1;
                } else {
                    throw new SongException("Song does not exist");
                }
            }else{
                throw new PlaylistException("Playlist does not exist");
            }

        }else{
            throw new UserException(STR."User with email: \{email} not found");
        }
    }

    public Playlist readPlaylist(String email) throws UserException {
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null){
                    return playlist1;
                }else{
                    throw new UserException("Playlist does not exist");
                }
            }else{
                throw new UserException(STR."User with email: \{email} not found");
            }

    }


    @Transactional
    public Playlist deletePlaylist(String email) throws UserException {
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null){
                    playlist1.getSongs().clear();
                    playlist1 = new Playlist();
                    playlistRepo.save(playlist1);
                    user.setPlaylist(playlist1);
                    userRepo.save(user);
                    return playlist1;
                }else{
                    throw new UserException("Playlist does not exist");
                }


            }else{
                throw new UserException("User: " + email + " not found");
            }

    }

    @Transactional
    public Playlist createPlaylist(String email, String playlist_name) throws UserException{
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                Playlist playlist1 = new Playlist();
                playlist1.setName(playlist_name);
                playlistRepo.save(playlist1);
                user.setPlaylist(playlist1);
                userRepo.save(user);

            }else{
                throw new UserException(STR."User with email: \{email} not found");
            }

        return null;

    }

    @Transactional
    public String renamePlaylist(String email, String playlist_name) throws UserException {
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                if(user.getPlaylist() != null){
                    user.getPlaylist().setName(playlist_name);
                    userRepo.save(user);
                    return "Your playlist has been renamed";
                }else{
                    throw new UserException("Playlist does not exist");
                }


            }else{
                throw new UserException(STR."User with email:\{email} not found");
            }



    }

    @Transactional
    public String clearPlaylist(String email) throws UserException {
            User user = userRepo.findByEmail(email).get();
            if(user != null) {
                Playlist playlist1 = user.getPlaylist();
                if(playlist1 != null){
                    playlist1.getSongs().clear();
                    playlistRepo.save(playlist1);
                    return "Your playlist has been cleared";
                }else{
                    throw new UserException("Playlist does not exist");
                }
            }else{
                throw new UserException(STR."User with email: \{email} not found");
            }

    }
    @Override
    public String getPlaylist(String email) throws UserException{
        User user1 = userRepo.findByEmail(email).get();
        if(user1 != null){
            if(user1.getPlaylist() != null){
                return user1.getPlaylist().toString();
            }else{
                throw new UserException("Playlist does not exist");
            }
        }else{
            throw new UserException(STR."User with email: \{email} not found");
        }

    }
    @Override
    public String getSongs(String email) throws UserException {
        User user1 = userRepo.findByEmail(email).get();
        if(user1 != null){
            if(user1.getPlaylist() != null){
                return user1.getPlaylist().toString();
            }else{
                throw new UserException("Playlist does not exist");
            }
        }else{
            throw new UserException("User with email: " + email + " is not found");
        }

    }

    
}
