package com.dvil.tracio.controller;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.service.RouteDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/route-details")
public class RouteDetailController {
    private final RouteDetailService routeDetailService;

    public RouteDetailController(RouteDetailService routeDetailService) {
        this.routeDetailService = routeDetailService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRouteDetails() {
        try {
            List<RouteDetailDTO> routeDetails = routeDetailService.getAllRouteDetails();
            return ResponseEntity.ok(routeDetails);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<?> getRouteDetailsByRouteId(@PathVariable Integer routeId) {
        try {
            List<RouteDetailDTO> routeDetails = routeDetailService.getRouteDetailsByRouteId(routeId);
            return ResponseEntity.ok(routeDetails);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteDetailById(@PathVariable Integer id) {
        try {
            RouteDetailDTO routeDetail = routeDetailService.getRouteDetailById(id);
            return ResponseEntity.ok(routeDetail);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRouteDetail(@RequestBody RouteDetailDTO routeDetailDTO) {
        try {
            RouteDetailDTO createdRouteDetail = routeDetailService.createRouteDetail(routeDetailDTO);
            return ResponseEntity.ok(Map.of("message", "Chi tiết lộ trình đã được tạo thành công!", "routeDetail", createdRouteDetail));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRouteDetail(@PathVariable Integer id, @RequestBody RouteDetailDTO routeDetailDTO) {
        try {
            RouteDetailDTO updatedRouteDetail = routeDetailService.updateRouteDetail(id, routeDetailDTO);
            return ResponseEntity.ok(Map.of("message", "Cập nhật chi tiết lộ trình thành công!", "routeDetail", updatedRouteDetail));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteDetail(@PathVariable Integer id) {
        try {
            routeDetailService.deleteRouteDetail(id);
            return ResponseEntity.ok(Map.of("message", "Chi tiết lộ trình với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
