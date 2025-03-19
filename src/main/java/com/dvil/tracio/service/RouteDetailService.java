package com.dvil.tracio.service;

import com.dvil.tracio.dto.RouteDetailDTO;
import java.util.List;

public interface RouteDetailService {
    List<RouteDetailDTO> getAllRouteDetails();
    RouteDetailDTO getRouteDetailById(Integer id);
    RouteDetailDTO createRouteDetail(RouteDetailDTO routeDetailDTO, Integer userId);
    RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO, Integer userId);
    void deleteRouteDetail(Integer id, Integer userId);
}
