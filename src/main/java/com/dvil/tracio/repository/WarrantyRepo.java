package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyRepo extends JpaRepository<Warranty, Integer> {
}
