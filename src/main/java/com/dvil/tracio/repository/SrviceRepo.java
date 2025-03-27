package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Srvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SrviceRepo extends JpaRepository<Srvice, Integer> {
    Optional<Srvice> findByServNameAndServDescription(String servName, String servDescription);
}
