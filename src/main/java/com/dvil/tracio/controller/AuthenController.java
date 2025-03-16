package com.dvil.tracio.controller;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.implementation.AuthenServiceImpl;
import com.dvil.tracio.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api/auth"})
public class AuthenController {


    private final UserRepo userRepo;

    private final AuthenServiceImpl authenService;
    private final JwtService jwtService;

    public AuthenController(UserRepo userRepo, AuthenServiceImpl authenService, JwtService jwtService) {
        this.userRepo = userRepo;
        this.authenService = authenService;
        this.jwtService = jwtService;
    }

    @PostMapping({"/register"})
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok(authenService.Register(request));
    }


    @PostMapping({"/login"})
    public String login(@RequestBody LoginRequest request) throws Exception {
        System.out.println(request.getUsername());
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return authenService.authenticate(request);
    }


}
