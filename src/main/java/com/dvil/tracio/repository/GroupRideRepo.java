package com.dvil.tracio.repository;

import com.dvil.tracio.entity.GroupRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRideRepo extends JpaRepository<GroupRide, Integer> {
}
