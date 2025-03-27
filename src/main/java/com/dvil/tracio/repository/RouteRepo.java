package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepo extends JpaRepository<Route, Integer> {
    List<Route> findByUserId(Integer userId);
}
