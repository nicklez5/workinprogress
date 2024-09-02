package com.spotify11.demo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.FileRepository;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.repo.SongRepo;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SongImpl implements SongService {

    private static final Logger log = LoggerFactory.getLogger(SongImpl.class);

    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;
    private final SongRepo songRepo;
    private final FileRepository fileRepo;
    private final Path fileStorageLocation;
    private final FileRepository fileRepository;

    public SongImpl(SessionRepo sessionRepo, UserRepo userRepo, SongRepo songRepo, FileRepository fileRepo, FileRepository fileRepository, Path fileStorageLocation) {

        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.songRepo = songRepo;
        this.fileRepo = fileRepo;
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths
    }




    @Override
    public Song createSong(String title, String artist, MultipartFile file, String uuId) throws CurrentUserException, UserException, IOException, SongException {
        Optional<CurrentUserSession> optionalSession = this.sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findByEmail(currentUserSession.getEmail());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (file != null) {
                    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                    String fileName = "";
                    try{
                        if(originalFilename.contains("..")){
                            throw new SongException("Bad file");
                        }
                        String fileExtension = "";
                        try{
                            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        }catch(Exception e){
                            fileExtension = "";
                        }


                    }
                    Files files = Files.builder().name(file.getOriginalFilename()).type(file.getContentType()).songData(file.getBytes()).build();
                    files = fileRepo.save(files);
                    log.info("File uploaded successfully into database with id{}", files.getId());
                    Song song = new Song((int)this.songRepo.count(), title, artist, files);
                    user.getLibrary().addSong(song);
                    userRepo.save(user);
                    return song;
                }else {
                    throw new IOException("File not found");
                }
            } else {

                throw new UserException("User is not logged in");
            }
        }else{
            throw new CurrentUserException("cant find User:" + uuId);
        }

    }

    @Override
    public Song readSong(String fileName, String uuId) throws UserException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Song> xyz2 = user.getLibrary().getSongs();
                for (Song song : xyz2) {
                    if (song.getFile().getName().equals(fileName)) {
                        return song;
                    }
                }
            } else {
                throw new UserException("User not found");
            }

        } else {
            throw new UserException("User is not present");
        }
        return null;
    }

    @Override
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String uuId) throws UserException, SongException, IOException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Song> xyz = user.getLibrary().getSongs();
                Song song = xyz.get(song_id);
                if (song != null && xyz.contains(song)) {
                    user.getLibrary().getSongs().get(song_id).setTitle(title);
                    user.getLibrary().getSongs().get(song_id).setArtist(artist);
                    if (file != null) {
                        String filePath = FILE_PATH + file.getOriginalFilename();
                        Files files = Files.builder().name(file.getOriginalFilename()).path(filePath).type(file.getContentType()).songData(file.getBytes()).build();
                        files = fileRepo.save(files);
                        user.getLibrary().getSongs().get(song_id).setFile(files);
                        file.transferTo(new File(filePath));
                        userRepo.save(user);
                        return song;
                    } else {
                        throw new IOException("File not found");
                    }
                } else {
                    throw new SongException("Song not found");
                }
            } else {
                throw new UserException("User is not logged in");
            }
        }
        return null;
    }
    @Override
    public String addFileToLibrary(MultipartFile file) throws IOException{
        String filePath = FILE_PATH + file.getOriginalFilename();
        Files files = Files.builder().name(file.getOriginalFilename()).type(file.getContentType()).songData(file.getBytes()).build();
        files = fileRepo.save(files);
        file.transferTo(new File(filePath));
        if(files.getId() != null){
            return "File uploaded to library successfully";
        }else{
            return "File uploaded to library failed";
        }
    }
    @Override
    public byte[] getFilefromSong(Song song1){
        return fileRepo.findByName(song1.getFile().getName()).getSongData();
    }
    @Override
    public byte[] downloadFileFromLibrary(String fileName) throws IOException{
        String path = fileRepository.findByName(fileName).getPath();
        return java.nio.file.Files.readAllBytes(new File(path).toPath());
    }

    @Override
    public void deleteSong(Song song, String uuId) throws  UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Song> songs = user.getLibrary().getSongs();
                if(songs.contains(song)){
                    user.getLibrary().getSongs().remove(song);
                    userRepo.save(user);
                    songRepo.delete(song);
                }else{
                    throw new SongException("Song not found");
                }
            } else {
                throw new UserException("User not found");
            }
        } else {
            throw new UserException("User is not present");
        }

    }



    @Override
    public Song getSong(int id, String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getLibrary().getSongs().get(id) != null) {
                    return user.getLibrary().getSongs().get(id);
                } else {
                    throw new SongException("Song not found");
                }
            } else {
                throw new UserException("User not found");
            }

        } else {
            throw new UserException("User is not present");
        }
    }

    @Override
    public Song getSong(String title, String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getLibrary().getSongs() != null) {
                    List<Song> xyz2 = user.getLibrary().getSongs();
                    for (Song song : xyz2) {
                        if (song.getTitle().equals(title)) {
                            return song;
                        }
                    }
                } else {
                    throw new SongException("Library is empty");
                }

            } else {
                throw new UserException("User is not present");
            }

        } else {
            throw new UserException("User is not logged in");


        }
        return null;
    }


    @Override
    public List<Song> getAllSongs(String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findById(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getLibrary().getSongs() != null) {
                    return user.getLibrary().getSongs();
                }else{
                    throw new SongException("Song not found");
                }
            }else{
                throw new UserException("User not found");
            }
        }else{
            throw new UserException("User is not logged in");
        }
    }
}