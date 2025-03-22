package com.dvil.tracio.controller;


import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.response.CreateShopResponse;
import com.dvil.tracio.service.implementation.AdminServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping({"/api/admin"})
public class AdminController {

    private final AdminServiceImpl adminService;
    private final UserRepo userRepository;

    public AdminController(AdminServiceImpl adminService, UserRepo userRepositor) {
        this.adminService = adminService;
        this.userRepository = userRepositor;
    }

//    @PostMapping("/shops/request")
//    public ResponseEntity<CreateShopResponse> createShopRequest(@RequestBody CreateShopRequest request,
//                                                                @AuthenticationPrincipal UserDetails userDetails) {
//        User owner = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        CreateShopResponse response = adminService.handleCreateShopRequest(request, owner.getId());
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/create-admin")
    public ResponseEntity<AdminRegisterResponse> createAdminAccount(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.createAdminAccount(request));
    }

}
