package com.spotify11.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotify11.demo.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    public Optional<User> findByEmail(String email);
}
