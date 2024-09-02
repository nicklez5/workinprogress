package com.spotify11.demo.repo;

import com.spotify11.demo.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<Files, Long> {
    Files findByName(String name);
}
