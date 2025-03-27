package com.dvil.tracio.controller;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.Order;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.OrderMapper;
import com.dvil.tracio.repository.OrderRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.OrderService;
import com.dvil.tracio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    Logger logger = Logger.getLogger(StaffController.class.getName());

    private final OrderMapper orderMapper;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;


}
