package com.spotify11.demo.repo;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spotify11.demo.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmail(String email);
    Users findByUsername(String username);
}
