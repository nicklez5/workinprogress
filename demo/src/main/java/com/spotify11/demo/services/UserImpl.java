package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserImpl implements UserService{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private PlaylistRepo playlistRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserImpl(UserRepo userRepo, SessionRepo sessionRepo, PlaylistRepo playlistRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.playlistRepo = playlistRepo;
    }

    //    @Override
//    public String addUser(){
//        return "Fake";
//    }
    @Transactional
    public Users addUser(Users user) throws UserException {
        if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")) {
            Library library1 = new Library();
            List<Playlist> playlist1 = new ArrayList<>();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setLibrary(library1);
            user.setPlaylists(playlist1);
            userRepo.save(user);
            return user;

        }
        else{
            throw new UserException("Enter correct role");
        }

    }
    @Override
    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "fail";
    }
    @Override
    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public Users updateUser(Users user, String uuId) throws CurrentUserException {
        Users user1 = this.getUser(uuId);
        if(user1 != null && user.getRole() != null){
            if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")){
                if(user1.getId() == user1.getId()){
                    Users user2 = userRepo.save(user);
                    return user2;
                }else{
                    throw new CurrentUserException("User Id is not same");
                }
            }else{
                throw new CurrentUserException("You dont have access");

            }


        }else{
            throw new CurrentUserException("Wrong role or Role is null");
        }

    }

    private Users getUser(String uuId) {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);
        if(optionalSession.isPresent()){
            CurrentUserSession currentUserSession = optionalSession.get();
            Optional<Users> optionalUser = userRepo.findById(currentUserSession.getUserId());
            if(optionalUser.isPresent()){
                return optionalUser.get();
            }
        }
        return null;

    }

    @Override
    public Users readUser(String uuId) throws CurrentUserException {
        return this.getUser(uuId);

    }
    // id is the one we are looking to delete
    // uuID is the parent
    @Override
    public Users deleteUser(String uuId, Integer id) throws CurrentUserException, UserException {
        Users user = this.getUser(uuId);
        if (user.getRole().equals("ADMIN")) {
            Optional<Users> optionalUser = userRepo.findById(id);
            if (optionalUser.isPresent()) {
                Users user1 = optionalUser.get();
                Optional<CurrentUserSession> optionalSession = sessionRepo.findById(user1.getEmail());
                if (optionalSession.isPresent()) {
                    this.logOut(optionalSession.get().getUuId());
                }
                userRepo.delete(user1);
                return user1;
            } else {
                throw new UserException("Wrong id");
            }
        } else {
            throw new UserException("You dont have access");
        }

    }

    @Override
    public CurrentUserSession logIn(Login logIn) throws CurrentUserException {

        Optional<Users> optionalUser = userRepo.findByEmail(logIn.getEmail());
        if(optionalUser.isPresent()){
            Users user = optionalUser.get();
            if(user.getPassword().equals(logIn.getPassword())){
                Optional<CurrentUserSession> optionalSession = sessionRepo.findById(logIn.getEmail());
                if(optionalSession.isEmpty()){
                    String key = this.randomString();
                    CurrentUserSession session = new CurrentUserSession(logIn.getEmail(),user.getId(),key);
                    return sessionRepo.save(session);

                }else{
                    throw new CurrentUserException("User already present");
                }
            }else{
                throw new CurrentUserException("Password is wrong");
            }
        }else{
            throw new CurrentUserException("Email is wrong");
        }

    }

    @Override
    public String logOut(String uuId) throws CurrentUserException {
        Optional<CurrentUserSession> optionalSession = sessionRepo.findByUuId(uuId);

        if(optionalSession.isPresent()){
            CurrentUserSession session = optionalSession.get();
            sessionRepo.delete(session);
            return "LogOut " + session.getEmail();
        }else{
            throw new CurrentUserException("Wrong UUID");
        }


    }

    @Override
    public Users assignPlaylistToUser(String uuId, Integer playlist_id) throws PlaylistException {
        List<Playlist> playlistSet = null;
        Users user = this.getUser(uuId);
        assert user != null;
        if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")){
            Playlist playlist = playlistRepo.findById(playlist_id).orElseThrow(null);
            playlistSet = user.getPlaylists();
            playlistSet.add(playlist);
            user.setPlaylists(playlistSet);
            userRepo.save(user);
            return user;
        }
        return null;
    }

    private String randomString(){
        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789@#$%&";
        int length = 6;
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for(int i = 0 ; i < length; i++){
            int index = random.nextInt(alphabets.length());
            str.append(alphabets.charAt(index));
        }
        return str.toString();
    }



}
