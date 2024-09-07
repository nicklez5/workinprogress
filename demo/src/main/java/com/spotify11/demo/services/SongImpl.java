package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.persistence.Id;
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


    private final SessionRepo sessionRepo;


    private final UserRepo userRepo;


    private final SongRepo songRepo;

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
            Optional<Users> optionalUser = userRepo.findByEmail(currentUserSession.getEmail());

            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
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
                        UploadFileResponse xyz3 = new UploadFileResponse(fileName, fileDownloadUri,file.getContentType(),file.getSize());

                        Song song123 = new Song(title,artist,fileDownloadUri,fileName);
                        user.getLibrary().addSong(song123);
                        //songRepo.save(song123);
                        userRepo.save(user);
                        return xyz3;



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


    @Transactional
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String uuId) throws UserException, SongException, FileStorageException, IOException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                List<Song> xyz = user.getLibrary().getSongs();
                song_id = song_id - 1;
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
                            user.getLibrary().getSongs().get(song_id).setFilename(fileName);
                            user.getLibrary().getSongs().get(song_id).setFileDownloadUri(fileDownloadUri);
                            Song song123 = user.getLibrary().getSongs().get(song_id);
                            userRepo.save(user);
                            //songRepo.save(song123);
                            return song123;
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
    @Override
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


    @Transactional
    public Song deleteSong(int song_id, String uuId) throws  UserException, SongException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                List<Song> songs = user.getLibrary().getSongs();
                Song song1 = user.getLibrary().getSongs().get(song_id);
                if(songs.contains(song1)){
                    user.getLibrary().getSongs().remove(song_id);
                    userRepo.save(user);
                    return song1;

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
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
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
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
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
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if (optionalSession.isPresent()) {
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
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