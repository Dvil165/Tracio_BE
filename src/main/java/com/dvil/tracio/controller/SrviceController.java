package com.dvil.tracio.controller;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.SrviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/services")
public class SrviceController {
    private final SrviceService srviceService;
    private final UserRepo userRepo;

    public SrviceController(SrviceService srviceService, UserRepo userRepo) {
        this.srviceService = srviceService;
        this.userRepo = userRepo;
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

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('SHOP_OWNER', 'ADMIN')")
    public ResponseEntity<?> createService(@RequestBody SrviceDTO srviceDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User owner = userRepo.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            Shop shop = owner.getShop();
            SrviceDTO createdService = srviceService.createService(srviceDTO, shop);

            if (createdService == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "message", "Không thể tạo dịch vụ, vui lòng thử lại!"
                ));
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Dịch vụ đã được tạo thành công!",
                    "service", createdService
            ));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "message", ex.getReason()
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Đã có lỗi xảy ra, vui lòng thử lại sau!"
            ));
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
