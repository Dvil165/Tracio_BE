package com.dvil.tracio.controller;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.service.RouteDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route-details")
public class RouteDetailController {
    private final RouteDetailService routeDetailService;

    public RouteDetailController(RouteDetailService routeDetailService) {
        this.routeDetailService = routeDetailService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDetailDTO>> getAllRouteDetails() {
        return ResponseEntity.ok(routeDetailService.getAllRouteDetails());
    }

    @PostMapping
    public ResponseEntity<RouteDetailDTO> createRouteDetail(@RequestBody RouteDetailDTO routeDetailDTO, @RequestParam Integer userId) {
        return ResponseEntity.ok(routeDetailService.createRouteDetail(routeDetailDTO, userId));
    }
}
