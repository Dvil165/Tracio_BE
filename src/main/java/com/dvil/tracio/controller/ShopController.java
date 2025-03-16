package com.dvil.tracio.controller;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity<ShopDTO> createShop(@RequestBody ShopDTO shopDTO, @PathVariable Integer ownerId) {
        return ResponseEntity.ok(shopService.createShop(shopDTO, ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(shopService.getShopById(id));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @GetMapping
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ShopDTO>> getShopsByOwnerId(@PathVariable Integer ownerId) {
        return ResponseEntity.ok(shopService.getShopsByOwnerId(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateShop(@PathVariable Integer id, @RequestBody ShopDTO shopDTO) {
        ShopDTO updatedShop = shopService.updateShop(id, shopDTO);
        return ResponseEntity.ok(Map.of("message", "Cửa hàng ID " + id + " đã được cập nhật thành công!", "shop", updatedShop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteShop(@PathVariable Integer id) {
        String message = shopService.deleteShop(id);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
