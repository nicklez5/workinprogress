package com.spotify11.demo.services;

import com.spotify11.demo.entity.CurrentUserSession;
import com.spotify11.demo.entity.Login;
import com.spotify11.demo.entity.User;
import com.spotify11.demo.exception.CurrentUserException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.SessionRepo;
import com.spotify11.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.exception.PlaylistException;

@Service
public class UserImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

//    @Override
//    public String addUser(){
//        return "Fake";
//    }
    @Override
    public User addUser(User user) throws UserException {
        if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")) {
            User user1 = userRepo.save(user);
            return user1;
        }
        else{
            throw new UserException("Enter correct role");
        }

    }
    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepo.findAll();
        return userList;
    }
    @Override
    public User updateUser(User user, String uuId) throws CurrentUserException {
        User user1 = this.getUser(uuId);
        if(user1 != null && user.getRole() != null){
            if(user.getRole().equals("ADMIN") || user.getRole().equals("USER")){
                if(user1.getId() == user1.getId()){
                    User user2 = userRepo.save(user);
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

    @Override
    public User readUser(String uuId) throws CurrentUserException {
        User user = this.getUser(uuId);
        if(user != null){
            return user;
        }else{
            throw new CurrentUserException("User is not present");
        }

    }
    // id is the one we are looking to delete
    // uuID is the parent
    @Override
    public User deleteUser(String uuId,Integer id) throws CurrentUserException, UserException {
        User user = this.getUser(uuId);
        if(user != null){
            if(user.getRole().equals("ADMIN")){
                Optional<User> optionalUser = userRepo.findById(id);
                if(optionalUser.isPresent()){
                    User user1 = optionalUser.get();
                    Optional <CurrentUserSession> optionalSession = sessionRepo.findById(user1.getEmail());
                    if(optionalSession.isPresent()){
                        this.logOut(optionalSession.get().getUuId());
                    }
                    userRepo.delete(user1);
                    return user1;
                }else{
                    throw new UserException("Wrong id");
                }
            }else{
                throw new UserException("You dont have access");
            }

        }else{
            throw new CurrentUserException("User is not present");
        }

    }
    @Override
    public User setPlaylist(Playlist playlist1, Integer uuId) throws CurrentUserException, PlaylistException{
        User user1 = userRepo.getById(uuId);
        if(user1 != null){
            if(playlist1 != null){
                user1.setPlaylist(playlist1);
                userRepo.save(user1);
                return user1;
            }else{
                throw new PlaylistException("Playlist is null");
            }
        }
        else{
            throw new CurrentUserException("User is Null");
        }
    }
    @Override
    public CurrentUserSession logIn(Login logIn) throws CurrentUserException {

        Optional<User> optionalUser = userRepo.findByEmail(logIn.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
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
    private User getUser(String uuId){
        return getUser(uuId, sessionRepo, userRepo);
    }
}
