package com.spotify11.demo.services;

import com.spotify11.demo.entity.*;
import com.spotify11.demo.exception.CurrentUserException;

import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserImpl implements UserService{
    
   


    @Autowired
    private JWTService jwtService;

    @Autowired
    public AuthenticationManager authManager;

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    
    public Users register(Users user) throws UserException {
            Library library1 = new Library();
            Set<Playlist> playlist1 = new HashSet<>();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setLibrary(library1);
            user.setPlaylists(playlist1);
            userRepo.save(user);
            return user;


    }
    
    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        else{
            return "fail";
        }
        
    }
    @Override
    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public Users updateUser(String username, String password, String role, String email) throws CurrentUserException {
        Users user1 = userRepo.findByUsername(username);
        user1.setPassword(encoder.encode(password));
        user1.setRole(role);
        user1.setEmail(email);
        return userRepo.save(user1);

    }

    private Users getUser(String username) {
        return userRepo.findByUsername(username);

    }


    @Override
    public Users readUser(String username)  {
        return this.getUser(username);

    }
    // id is the one we are looking to delete
    // uuID is the parent
    @Override
    public Users deleteUser(String username, Integer user_id)  {
        Users user = userRepo.findByUsername(username);
        Users admin = null;
        if(userRepo.findById(user_id).isPresent()){
            admin = userRepo.findById(user_id).get();
        }

        assert admin != null;
        if(admin.getRole().equals("ADMIN")) {
            userRepo.delete(user);
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
