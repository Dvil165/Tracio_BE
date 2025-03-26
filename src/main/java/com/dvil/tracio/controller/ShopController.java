package com.dvil.tracio.controller;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.CreateEmployeeRequest;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.CreateShopResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.ShopService;
import com.dvil.tracio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepo userRepo;

    public ShopController(ShopService shopService, UserRepo userRepo) {
        this.shopService = shopService;
        this.userRepo = userRepo;
    }


    @PostMapping("/{ownerId}")
    public ResponseEntity<String> createShop(@RequestBody ShopDTO shopDTO, @PathVariable Integer ownerId) {
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
    public ResponseEntity<ShopDTO> getShopsByOwnerId(@PathVariable Integer ownerId) {
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

    @PostMapping("/{shopId}/employees/create")
    public ResponseEntity<RegisterResponse> createEmployee(@PathVariable Integer shopId,
                                                           @RequestBody CreateEmployeeRequest request,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        User owner = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(shopService.createEmployee(shopId, request, owner));
    }

    @GetMapping("/{shopId}/employees")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    public ResponseEntity<List<UserDTO>> getEmployeesByShop(@PathVariable Integer shopId,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        User owner = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(shopService.getEmployeesByShop(shopId, owner));
    }

}
