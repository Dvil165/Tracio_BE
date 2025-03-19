package com.dvil.tracio.controller;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.service.implementation.UserServiceImplemented;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/user"})
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User with ID " + id + " has been deleted successfully.");
    }




}
