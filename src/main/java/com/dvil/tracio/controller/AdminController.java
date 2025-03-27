package com.dvil.tracio.controller;


import com.dvil.tracio.dto.ShopRequestDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.service.implementation.AdminServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping({"/api/admin"})
public class AdminController {

    private final AdminServiceImpl adminService;
    private final UserRepo userRepo;

    public AdminController(AdminServiceImpl adminService, UserRepo userRepo) {
        this.adminService = adminService;
        this.userRepo = userRepo;
    }


    @GetMapping("/shops/request")
    public ResponseEntity<List<ShopRequestDTO>> getAllShopRequest() {
        List<ShopRequestDTO> requests = adminService.getAllRequest();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<AdminRegisterResponse> createAdminAccount(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.createAdminAccount(request));
    }

    @PostMapping("shops/request/{requestId}/approve")
    public ResponseEntity<String> approveShopRequest(
            @PathVariable Integer requestId,
            @AuthenticationPrincipal UserDetails adminDetails) {
        User admin = userRepo.findByUsername(adminDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String response = adminService.approveShopRequest(requestId, admin);
        return ResponseEntity.ok(response);
    }

    @PostMapping("shops/request/{requestId}/reject")
    public ResponseEntity<String> rejectShopRequest(
            @PathVariable Integer requestId,
            @AuthenticationPrincipal UserDetails adminDetails) {
        User admin = userRepo.findByUsername(adminDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String response = adminService.rejectShopRequest(requestId, admin);
        return ResponseEntity.ok(response);
    }

}
