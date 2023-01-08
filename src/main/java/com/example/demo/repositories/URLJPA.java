package com.example.demo.repositories;

import com.example.demo.models.URLLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "urls")
public interface URLJPA extends JpaRepository<URLLong, Long> {

    Optional<URLLong> findByKeySite(String keySite);
}
