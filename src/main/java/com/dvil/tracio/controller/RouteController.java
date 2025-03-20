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
    public ResponseEntity<?> getAllRoutes() {
        try {
            List<RouteDTO> routes = routeService.getAllRoutes();
            return ResponseEntity.ok(routes);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable Integer id) {
        try {
            RouteDTO route = routeService.getRouteById(id);
            return ResponseEntity.ok(route);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody RouteDTO routeDTO) {
        try {
            RouteDTO createdRoute = routeService.createRoute(routeDTO);
            return ResponseEntity.ok(Map.of("message", "Lộ trình đã được tạo thành công!", "route", createdRoute));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Integer id, @RequestBody RouteDTO routeDTO) {
        try {
            RouteDTO updatedRoute = routeService.updateRoute(id, routeDTO);
            return ResponseEntity.ok(Map.of("message", "Cập nhật lộ trình thành công!", "route", updatedRoute));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Integer id) {
        try {
            routeService.deleteRoute(id);
            return ResponseEntity.ok(Map.of("message", "Lộ trình với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
