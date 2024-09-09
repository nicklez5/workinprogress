package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.Users;
import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepo;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


@Service
public class SongImpl implements SongService {

    private static final Logger log = LoggerFactory.getLogger(SongImpl.class);


    private final UserRepo userRepo;


    private final SongRepo songRepo;

    private final Path fileStorageLocation;



    public SongImpl(UserRepo userRepo, SongRepo songRepo, FileStorageProperties fileStorageProperties) {
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
    public UploadFileResponse createSong(String title, String artist, MultipartFile file, String username) throws Exception {
            Users user = userRepo.findByUsername(username);
            if(user != null){
                if(file != null){
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
                        UploadFileResponse xyz3 = new UploadFileResponse((int) songRepo.count()+1, fileName, fileDownloadUri, file.getContentType(), file.getSize());


                        Song song123 = new Song(title, artist, fileDownloadUri, fileName);

                        user.getLibrary().addSong(song123);
                        songRepo.saveAndFlush(song123);

                        userRepo.save(user);
                        return xyz3;
                    }catch(IOException ex){
                        throw new FileStorageException("Could not store file " + fileName + ".Please try again!", ex);
                    }
                }else {
                    throw new FileNotFoundException("File not found.");
                }
            }else {
                throw new Exception("cannot find username: " + username);
            }


    }


    @Transactional
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String username) throws UserException, SongException, FileStorageException, IOException {
            Users user = userRepo.findByUsername(username);
            if(user != null){
                List<Song> xyz = user.getLibrary().getSongs();
                song_id = song_id - 1;
                Song song = xyz.get(song_id);
                if(song != null && xyz.contains(song)){
                    user.getLibrary().getSongs().get(song_id).setTitle(title);
                    user.getLibrary().getSongs().get(song_id).setArtist(artist);
                    if(file != null){
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
                    }else {
                        throw new FileNotFoundException("File not found.");
                    }
                }else {
                        throw new SongException("Song not found in library or is null");
                }

            }else {
                throw new UserException("Username: " + username + " not found");
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
    public Song deleteSong(int song_id, String username) throws  UserException, SongException {
            Users user = userRepo.findByUsername(username);
            if(user != null){
                List<Song> xyz = user.getLibrary().getSongs();
                song_id = song_id - 1;
                Song song = xyz.get(song_id);
                if(xyz.contains(song)){
                    user.getLibrary().getSongs().remove(song_id);
                    userRepo.save(user);
                    return song;
                }else{
                    throw new SongException("Song not found in library or is null");
                }
            }else{
                throw new UserException("Username: " + username + " not found");
            }

    }


    @Override
    public Song getSong(int song_id, String username) throws UserException, SongException {
        Users user = userRepo.findByUsername(username);
        if(user != null){
            List<Song> xyz = user.getLibrary().getSongs();
            for(Song song : xyz){
                if (song.getId() == song_id){
                    return song;
                }
            }
        }else {
            throw new UserException("Username: " + username + " not found");
        }
        return null;
    }

    @Override
    public Song getSong(String title, String username) throws UserException, SongException {
        Users user = userRepo.findByUsername(username);
        if(user != null){
            List<Song> xyz = user.getLibrary().getSongs();
            for (Song song : xyz) {
                if (song.getTitle().equals(title)) {
                    return song;
                }
            }
        }else{
            throw new UserException("Username: " + username + " not found");
        }

        return null;
    }


    @Override
    public List<Song> getAllSongs(String username) throws UserException, SongException {
        Users user = userRepo.findByUsername(username);
        if(user != null){
            List<Song> xyz = user.getLibrary().getSongs();
            return xyz;
        }else {
            throw new UserException("Username: " + username + " not found");
        }
    }
}