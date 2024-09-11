package com.spotify11.demo.services;

import com.spotify11.demo.dtos.LoginUserDto;
import com.spotify11.demo.dtos.RegisterUserDto;
import com.spotify11.demo.entity.*;


import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserImpl implements UserService{
    


    @Autowired
    private UserRepository userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public User addUser(RegisterUserDto input) throws UserException {
        User xyz = new User();
        xyz.setEmail(input.getEmail());
        xyz.setPassword(encoder.encode(input.getPassword()));
        xyz.setFullName(input.getFullName());
        userRepo.save(xyz);
        return xyz;
    }

    @Override
    public List<User> getAllUser() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public User updateUser(String fullName, String password, String email) throws UserException {
        User xyz = userRepo.findByEmail(email).get();
        xyz.setEmail(email);
        xyz.setPassword(encoder.encode(password));
        xyz.setFullName(fullName);
        return xyz;
    }



    @Override
    public User readUser(String email) throws UserException {
        return userRepo.findByEmail(email).get();

    }
    // id is the one we are looking to delete
    // uuID is the parent
    @Override
    public User deleteUser(String email) throws UserException {
        User user = userRepo.findByEmail(email).get();
        if(user != null){
            userRepo.delete(user);
        }else{
            throw new UserException("User not found");
        }
        return null;



    }

    @Override
    public User authenticate(LoginUserDto input) throws UserException {
        User user1 = userRepo.findByEmail(input.getEmail()).get();
        if(user1.isEnabled()){
            return user1;
        }else{
            throw new UserException("Could not authenticate");
        }

    }






}
