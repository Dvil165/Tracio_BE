package com.dvil.tracio.service;

import com.dvil.tracio.dto.RouteDTO;
import java.util.List;

public interface RouteService {
    List<RouteDTO> getAllRoutes();
    RouteDTO getRouteById(Integer id);
    RouteDTO createRoute(RouteDTO routeDTO);
    RouteDTO updateRoute(Integer id, RouteDTO routeDTO);
    void deleteRoute(Integer id);
}
