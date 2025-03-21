package com.dvil.tracio.service;

import com.dvil.tracio.dto.RouteDTO;
import java.util.List;

public interface RouteService {
    RouteDTO createRoute(RouteDTO routeDTO);
    List<RouteDTO> getAllRoutes();
    RouteDTO getRouteById(Integer id);
    RouteDTO updateRoute(Integer id, RouteDTO routeDTO);
    void deleteRoute(Integer id);
}
