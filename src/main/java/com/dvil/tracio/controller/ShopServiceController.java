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
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @PostMapping
    public ResponseEntity<?> createShopService(@RequestBody ShopServiceDTO shopServiceDTO) {
        try {
            ShopServiceDTO createdShopService = shopServiceService.createShopService(shopServiceDTO);
            return ResponseEntity.ok(Map.of("message", "Dịch vụ của cửa hàng đã được tạo thành công!", "shopService", createdShopService));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShopService(@PathVariable Integer id) {
        try {
            shopServiceService.deleteShopService(id);
            return ResponseEntity.ok(Map.of("message", "Dịch vụ của cửa hàng với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
