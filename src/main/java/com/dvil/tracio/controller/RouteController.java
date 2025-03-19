package com.dvil.tracio.controller;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody RouteDTO routeDTO, @RequestParam Integer userId) {
        try {
            RouteDTO createdRoute = routeService.createRoute(routeDTO, userId);
            return ResponseEntity.ok(Map.of("message", "Tuyến đường đã được tạo thành công!", "route", createdRoute));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Integer id, @RequestBody RouteDTO routeDTO, @RequestParam Integer userId) {
        try {
            RouteDTO updatedRoute = routeService.updateRoute(id, routeDTO, userId);
            return ResponseEntity.ok(Map.of("message", "Cập nhật tuyến đường thành công!", "route", updatedRoute));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Integer id, @RequestParam Integer userId) {
        try {
            routeService.deleteRoute(id, userId);
            return ResponseEntity.ok(Map.of("message", "Tuyến đường với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
