package com.dvil.tracio.repository;

import com.dvil.tracio.entity.GroupRide;
import com.dvil.tracio.entity.GroupRideJoiner;
import com.dvil.tracio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRideJoinerRepo extends JpaRepository<GroupRideJoiner, Integer> {
    boolean existsByGroupRideAndUser(GroupRide groupRide, User user);
    Optional<GroupRideJoiner> findByGroupRideAndUser(GroupRide groupRide, User user);
    List<GroupRideJoiner> findByGroupRide(GroupRide groupRide);

}

