package com.dvil.tracio.service;

import com.dvil.tracio.dto.RouteDetailDTO;
import java.util.List;

public interface RouteDetailService {
    RouteDetailDTO addRouteDetail(RouteDetailDTO routeDetailDTO);
    List<RouteDetailDTO> getRouteDetailsByRouteId(Integer routeId);
    RouteDetailDTO getRouteDetailById(Integer id);
    RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO);
    void deleteRouteDetail(Integer id);
}
