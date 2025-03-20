package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.AdminService;
import com.dvil.tracio.service.EmailService;
import com.dvil.tracio.util.AuthenValidation;
import com.dvil.tracio.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenServiceImpl.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserRepo userRepository;
    private final JwtService jwtService;

    public AdminServiceImpl(UserRepo userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    @Override
    public AdminRegisterResponse createAdminAccount(AdminRegisterRequest adminRegisterRequest) {
        if (userRepository.findByEmail(adminRegisterRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng!"); // Hoặc dùng CustomException
        }


        Integer adminCount = userRepository.countByRole(RoleName.ADMIN.name());

        // Tạo User mới
        User newAdmin = new User();
        newAdmin.setEmail(adminRegisterRequest.getEmail());
        newAdmin.setUsername("admin" + (adminCount + 1));
        newAdmin.setUserPassword(encoder.encode(adminRegisterRequest.getPassword()));
        newAdmin.setPhone(adminRegisterRequest.getPhone());
        newAdmin.setCreatedAt(Instant.now());
        newAdmin.setAccountStatus(UserVerifyStatus.Verified);
        newAdmin.setRole(RoleName.ADMIN);
        // Tạo JWT Token
        String accessToken = jwtService.generateAccessToken(newAdmin);
        newAdmin.setAccessToken(accessToken);
        String refreshToken = jwtService.generateRefreshToken(newAdmin);
        newAdmin.setRefToken(refreshToken);

        // Lưu user vào DB
        userRepository.save(newAdmin);
        return new AdminRegisterResponse("Dang ki admin thanh cong", newAdmin.getUsername(), adminRegisterRequest.getPassword());
    }
}
