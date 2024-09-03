package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;


@Service
public class SongImpl implements SongService {

    private static final Logger log = LoggerFactory.getLogger(SongImpl.class);

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SongRepo songRepo;



    private final Path fileStorageLocation;



    public SongImpl(SessionRepo sessionRepo, UserRepo userRepo, SongRepo songRepo, FileStorageProperties fileStorageProperties) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.songRepo = songRepo;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence.");
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch(IOException ex){
            throw new FileStorageException("Could not store file " + fileName + ".Please try again!", ex);
        }
    }
    @Transactional
    public UploadFileResponse createSong(String title, String artist, MultipartFile file, String uuId) throws CurrentUserException,  SongException {
        Optional<CurrentUserSession> optionalSession = this.sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findByEmail(currentUserSession.getEmail());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (file != null) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    try {
                        if (fileName.contains("..")) {
                            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                        }
                        Path targetLocation = this.fileStorageLocation.resolve(fileName);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/downloadFile/")
                                .path(fileName)
                                .toUriString();
                        Integer count1 = user.getLibrary().getSongs().size();
                        Song song123 = new Song(count1+1,title,artist,fileDownloadUri);
                        user.getLibrary().addSong(song123);
                        songRepo.save(song123);
                        userRepo.save(user);
                        UploadFileResponse response1 = new UploadFileResponse(fileName,fileDownloadUri,file.getContentType(),file.getSize());
                        return response1;

                    } catch (IOException e) {
                        throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
                    }
                } else {
                    throw new SongException("Song is null");
                }
            } else {
                throw new CurrentUserException("cant find User:" + uuId);
            }

        }else{
            throw new CurrentUserException("No one is logged in");
        }

    }

//    @Override
//    public Song readSong(String title, String uuId) throws UserException {
//        Optional<CurrentUserSession> optionalSession = this.sessionRepo.findByUuId(uuId);
//        if (optionalSession.isPresent()) {
//            CurrentUserSession currentUserSession = optionalSession.get();
//            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                List<Song> xyz2 = user.getLibrary().getSongs();
//                for (Song song : xyz2) {
//                    if (song.getTitle().equals(title)) {
//                        return song;
//                    }
//                }
//            } else {
//                throw new UserException("User not found");
//            }
//
//        } else {
//            throw new UserException("User is not present");
//        }
//        return null;
//    }

    @Transactional
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String uuId) throws UserException, SongException, FileStorageException, IOException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
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
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                        try{
                            if (fileName.contains("..")) {
                                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                            }
                            Path targetLocation = this.fileStorageLocation.resolve(fileName);
                            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/downloadFile/")
                                    .path(fileName)
                                    .toUriString();
                            user.getLibrary().getSongs().get(song_id).setFileDownloadUri(fileDownloadUri);
                            userRepo.save(user);
                            return song;
                        }catch(IOException e){
                            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
                        }

                    } else {
                        throw new IOException("File is null");
                    }
                } else {
                    throw new SongException("Song not found");
                }
            } else {
                throw new UserException("User is not logged in");
            }
        }else{
            return null;
        }
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileNotFoundException("File not found " + fileName);
            }
        }catch(MalformedURLException ex){
            throw new FileNotFoundException("File not found" + fileName);
        }
    }

//    public String getSongName(String uuId, Song song1) throws CurrentUserException {
//        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
//        if (optionalSession.isPresent()) {
//            CurrentUserSession currentUserSession = optionalSession.get();
//            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//
//            }else{
//                throw new CurrentUserException("User is not logged in");
//            }
//        }else{
//            throw new CurrentUserException("User is not logged in");
//        }
//
//    }
    @Transactional
    public void deleteSong(Song song, String uuId) throws  UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<User> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Song> songs = user.getLibrary().getSongs();
                if(songs.contains(song)){
                    user.getLibrary().getSongs().remove(song);
                    userRepo.save(user);
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




    public Song getSong(int id, String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
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


    public Song getSong(String title, String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
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



    public List<Song> getAllSongs(String uuId) throws UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
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