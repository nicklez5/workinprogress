package com.spotify11.demo.services;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.PlaylistRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class PlaylistImpl implements PlaylistService {



    private final PlaylistRepo play_repo;

    @Autowired
    public PlaylistImpl(PlaylistRepo playRepo) {
        play_repo = playRepo;
    }


    @Override
    public String addSong(Song song1, Integer playlist_id) throws SongException {
        if(play_repo.findById(playlist_id).isPresent()){
            play_repo.findById(playlist_id).get().getSongs().add(song1);
            return song1.toString() + " has been added";
        }else{
            throw new SongException("Song was not found");
        }


    }

    @Override
    public String removeSong(Song song1, Integer playlistId) throws SongException {
        if (play_repo.findById(playlistId).isPresent()) {
            play_repo.findById(playlistId).get().getSongs().remove(song1);
            return song1.toString() + " has been deleted";
        }else{
            throw new SongException("Song was not found");

        }


    }

    @Override
    public Set<Song> getSongs() {
        return this.getSongs();
    }

    @Override
    public void deletePlaylist(Integer playlist_id) throws PlaylistException {
        if(play_repo.findById(playlist_id).isPresent()) {
            Playlist ply1 = play_repo.findById(playlist_id).get();
            play_repo.deleteById(playlist_id);
        } else{
            throw new PlaylistException("Playlist was not found");
        }
    }

    @Override
    public void readPlaylist(Integer playlist_id) {
        while(play_repo.findById(playlist_id).isPresent()){
            play_repo.findById(playlist_id).ifPresent(song -> {
                final String string = song.toString();
                System.out.println(string);

            });
        }
    }


    @Override
    public void renamePlaylist(String playlist_name, Integer playlistId) throws PlaylistException {
            if(play_repo.findById(playlistId).isPresent()){
                play_repo.findById(playlistId).get().setName(playlist_name);
                play_repo.findById(playlistId).get().toString();
            }else{
                throw new PlaylistException("Playlist id: " + playlistId + " was not found");
            }
    }

    @Override
    public void clearPlaylist(Integer playlist_id) throws PlaylistException {
            if(play_repo.findById(playlist_id).isPresent()){
                play_repo.findById(playlist_id).get().getSongs().clear();
                System.out.println("Playlist id" + playlist_id + "has been deleted");
            }else{
                throw new PlaylistException("Playlist was not found");
            }
    }

    @Override
    public Set<Song> getPlaylist(Integer playlistId) throws PlaylistException {
        if(play_repo.findById(playlistId).isPresent()){
            play_repo.findById(playlistId).get().getSongs();
            return play_repo.findById(playlistId).get().getSongs();
        }else{
            throw new PlaylistException("Playlist was not found");
        }
    }
}
