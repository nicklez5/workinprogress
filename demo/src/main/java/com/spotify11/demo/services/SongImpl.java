package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;

import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepository;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;


@Service
public class SongImpl implements SongService {


    private final SongRepo songRepo;
    private final UserRepository userRepo;
    private final Path fileStorageLocation;


    @Value("${filePath}")
    private String basePath;

    @Autowired
    public SongImpl(SongRepo songRepo, UserRepository userRepo, FileStorageProperties fileStorageProperties) {
        this.songRepo = songRepo;
        this.userRepo = userRepo;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }


    @Transactional
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String email) throws UserException, SongException, FileStorageException {
        User user = userRepo.findByEmail(email).get();
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
                        user.getLibrary().getSongs().get(song_id).setFileDownloadUri(URI.create(fileDownloadUri));
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
            throw new UserException("Username with email: " + email + " not found");
        }

    }

    @Transactional
    @Override
    public UploadFileResponse createSong(String title, String artist, MultipartFile file, String email) throws Exception, FileNotFoundException, FileStorageException {
        User user = userRepo.findByEmail(email).get();
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


                    Song song123 = new Song(title, artist, fileName, URI.create(fileDownloadUri.toString()));

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
            throw new Exception("cannot find user with email: " + email);
        }


    }


    public Resource loadFileAsResource(String fileName) throws Exception {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new Exception("File not found " + fileName);
            }
        }catch(MalformedURLException ex){
            throw new Exception("File not found" + fileName);
        }
    }

    public void loadAllSongs(){
        File directory_path = new File(this.basePath);
        String contents[] = directory_path.list();
        for(int i = 0 ; i < contents.length ; i++){
            System.out.println(contents[i]);
        }

    }



    @Override
    public String getSong(String title) throws  SongException {
        if (songRepo.findByTitle(title).isPresent()) {
            Song song1 = songRepo.findByTitle(title).get();
            return song1.toString();
        }else{
            throw new SongException("Song not found");
        }
    }


    @Override
    public List<Song> getAllSongs() throws UserException, SongException {

        return songRepo.findAll();

    }
}