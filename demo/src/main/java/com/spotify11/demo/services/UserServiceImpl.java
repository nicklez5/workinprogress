package com.spotify11.demo.services;

import com.spotify11.demo.entity.User;
import com.spotify11.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Override
//    public String addUser(){
//        User user = new User("coders","coders@gmail.com","coders");
//        User user1 = new User("coder","coder@gmail.com","coder");
//        User user2 = userRepo.save(user);
//        User user3 = userRepo.save(user1);
//        return user2.toString() + " / " + user3.toString();
//    }
//
//    @Override
//    public List<User> getAllUser() {
//        List<User> userList = userRepo.findAll();
//        return userList;
//    }
//}
