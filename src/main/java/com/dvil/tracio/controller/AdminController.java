package com.dvil.tracio.controller;


import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.service.AdminService;
import com.dvil.tracio.service.implementation.AdminServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/admin"})
public class AdminController {

    private final AdminServiceImpl adminService;

    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create-admin")
    public AdminRegisterResponse createAdminAccount(@RequestBody AdminRegisterRequest request) {
        return adminService.createAdminAccount(request);
    }

}
