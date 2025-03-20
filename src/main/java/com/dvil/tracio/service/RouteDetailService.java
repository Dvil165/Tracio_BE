package com.dvil.tracio.service;

import com.dvil.tracio.dto.RouteDetailDTO;
import java.util.List;

public interface RouteDetailService {
    List<RouteDetailDTO> getAllRouteDetails();
    List<RouteDetailDTO> getRouteDetailsByRouteId(Integer routeId);
    RouteDetailDTO getRouteDetailById(Integer id);
    RouteDetailDTO createRouteDetail(RouteDetailDTO routeDetailDTO);
    RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO);
    void deleteRouteDetail(Integer id);
}
