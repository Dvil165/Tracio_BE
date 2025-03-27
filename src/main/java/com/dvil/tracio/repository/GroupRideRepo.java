package com.dvil.tracio.repository;

import com.dvil.tracio.entity.GroupRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRideRepo extends JpaRepository<GroupRide, Integer> {
    List<GroupRide> findByCreatedBy_Id(Integer userId);
}