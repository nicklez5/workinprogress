package com.spotify11.demo.repo;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spotify11.demo.entity.Library;



@Repository
public interface LibraryRepo extends CrudRepository<Library,Integer> {
    
    
}
