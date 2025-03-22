package com.dvil.tracio.controller;

import com.dvil.tracio.dto.ShopServiceDTO;
import com.dvil.tracio.service.ShopServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/shop-services")
public class ShopServiceController {
    private final ShopServiceService shopServiceService;

    public ShopServiceController(ShopServiceService shopServiceService) {
        this.shopServiceService = shopServiceService;
    }

    @GetMapping
    public ResponseEntity<?> getAllShopServices() {
        try {
            List<ShopServiceDTO> shopServices = shopServiceService.getAllShopServices();
            return ResponseEntity.ok(shopServices);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        }
    }

//    @PostMapping("/{serviceId}")
//    public ResponseEntity<?> createShopService(@PathVariable Integer serviceId) {
//        try {
//            ShopServiceDTO created = shopServiceService.createShopService(serviceId);
//            return ResponseEntity.ok(Map.of("message", "Tạo dịch vụ thành công", "shopService", created));
//        } catch (ResponseStatusException ex) {
//            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShopService(@PathVariable Integer id) {
        try {
            shopServiceService.deleteShopService(id);
            return ResponseEntity.ok(Map.of("message", "Xoá dịch vụ của shop thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        }
    }
}
