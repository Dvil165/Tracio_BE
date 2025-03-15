package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Srvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SrviceRepo extends JpaRepository<Srvice, Integer> {
}
