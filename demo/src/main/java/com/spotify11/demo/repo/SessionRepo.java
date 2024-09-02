package com.spotify11.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spotify11.demo.entity.CurrentUserSession;

@Repository
public interface SessionRepo extends CrudRepository<CurrentUserSession,String> {

        Optional<CurrentUserSession> findByUuId(String uuId);
}
