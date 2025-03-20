package com.dvil.tracio.repository;

import com.dvil.tracio.entity.RouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteDetailRepo extends JpaRepository<RouteDetail, Integer> {
    List<RouteDetail> findByRouteId(Integer routeId);
}
