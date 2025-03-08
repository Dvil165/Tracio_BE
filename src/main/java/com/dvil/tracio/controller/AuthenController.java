package com.dvil.tracio.controller;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.implementation.UserServiceImplemented;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/user"})
public class AuthenController {
    private final UserServiceImplemented userService;

    public AuthenController(UserServiceImplemented userService) {
        this.userService = userService;
    }

    @PostMapping({"/register"})
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok(this.userService.Register(request));
    }

    @GetMapping({"/all"})
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
