package com.dvil.tracio.controller;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.service.SrviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/services")
public class SrviceController {
    private final SrviceService srviceService;

    public SrviceController(SrviceService srviceService) {
        this.srviceService = srviceService;
    }

    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<SrviceDTO> services = srviceService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Integer id) {
        try {
            SrviceDTO service = srviceService.getServiceById(id);
            return ResponseEntity.ok(service);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody SrviceDTO srviceDTO) {
        try {
            SrviceDTO createdService = srviceService.createService(srviceDTO);
            return ResponseEntity.ok(Map.of("message", "Dịch vụ đã được tạo thành công!", "service", createdService));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable Integer id, @RequestBody SrviceDTO srviceDTO) {
        try {
            SrviceDTO updatedService = srviceService.updateService(id, srviceDTO);
            return ResponseEntity.ok(Map.of("message", "Cập nhật dịch vụ thành công!", "service", updatedService));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        try {
            srviceService.deleteService(id);
            return ResponseEntity.ok(Map.of("message", "Dịch vụ với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
