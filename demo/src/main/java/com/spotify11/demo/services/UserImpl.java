package com.spotify11.demo.services;

import com.spotify11.demo.dtos.LoginUserDto;
import com.spotify11.demo.dtos.RegisterUserDto;
import com.spotify11.demo.entity.*;


import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserImpl implements UserService{
    
    private final UserRepository userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    @Override
    public User addUser(RegisterUserDto input) throws UserException {
        User xyz = new User();
        xyz.setEmail(input.getEmail());
        xyz.setPassword(encoder.encode(input.getPassword()));
        xyz.setFullName(input.getFullName());
        userRepo.save(xyz);
        return xyz;
    }

    @Transactional
    @Override
    public List<User> getAllUser() {
        return (List<User>) userRepo.findAll();
    }

    @Transactional
    @Override
    public User updateUser(String fullName, String password, String email) throws UserException {

        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            user.setFullName(fullName);
            user.setPassword(encoder.encode(password));
            userRepo.save(user);
            return user;
        }else{
            throw new UserException("User not found");
        }



    }


    @Override
    public User readUser(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            return userRepo.findByEmail(email).get();
        }else{
            throw new UserException("User not found");
        }
    }
    // id is the one we are looking to delete

    // uuID is the parent
    @Override
    public String deleteUser(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()) {
            userRepo.delete(userRepo.findByEmail(email).get());
            return "User deleted";
        }else{
            throw new UserException("User with email: " + email + " not found");
        }



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
