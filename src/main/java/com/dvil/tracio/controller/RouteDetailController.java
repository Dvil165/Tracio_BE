package com.dvil.tracio.controller;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.service.RouteDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/route-details")
public class RouteDetailController {
    private final RouteDetailService routeDetailService;

    public RouteDetailController(RouteDetailService routeDetailService) {
        this.routeDetailService = routeDetailService;
    }

    @PostMapping
    public ResponseEntity<?> addRouteDetail(@RequestBody RouteDetailDTO routeDetailDTO) {
        RouteDetailDTO createdRouteDetail = routeDetailService.addRouteDetail(routeDetailDTO);
        return ResponseEntity.ok(Map.of("message", "RouteDetail đã được thêm thành công!", "routeDetail", createdRouteDetail));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<?> getRouteDetailsByRouteId(@PathVariable Integer routeId) {
        List<RouteDetailDTO> details = routeDetailService.getRouteDetailsByRouteId(routeId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteDetailById(@PathVariable Integer id) {
        RouteDetailDTO detail = routeDetailService.getRouteDetailById(id);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRouteDetail(@PathVariable Integer id, @RequestBody RouteDetailDTO routeDetailDTO) {
        RouteDetailDTO updatedDetail = routeDetailService.updateRouteDetail(id, routeDetailDTO);
        return ResponseEntity.ok(Map.of("message", "RouteDetail đã được cập nhật!", "routeDetail", updatedDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteDetail(@PathVariable Integer id) {
        routeDetailService.deleteRouteDetail(id);
        return ResponseEntity.ok(Map.of("message", "RouteDetail với ID " + id + " đã bị xóa thành công!"));
    }
}
