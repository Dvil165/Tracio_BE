package com.dvil.tracio.controller;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.service.implementation.UserServiceImplemented;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/user-controller"})
public class UserController {

    private final UserServiceImplemented userService;

    public UserController(UserServiceImplemented userService) {
        this.userService = userService;
    }

    @GetMapping({"/all"})
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
