package com.dvil.tracio.controller;


import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.CreateShopResponse;
import com.dvil.tracio.service.ShopRequestService;
import com.dvil.tracio.service.implementation.AuthenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/shops/requests")
public class ShopRequestController {
    private static final Logger logger = LoggerFactory.getLogger(ShopRequestController.class);

    private final ShopRequestService shopRequestService;
    private final UserRepo userRepo;

    public ShopRequestController(ShopRequestService shopRequestService, UserRepo userRepo) {
        this.shopRequestService = shopRequestService;
        this.userRepo = userRepo;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateShopResponse> requestCreateShop(@RequestBody CreateShopRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User owner = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        logger.info("Authenticated user: {}", userDetails.getUsername());
        return ResponseEntity.ok(shopRequestService.createShopRequest(request, owner));
    }

}
