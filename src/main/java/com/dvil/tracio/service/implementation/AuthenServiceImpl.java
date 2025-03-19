package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.entity.UserRole;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.AuthenticationService;
import com.dvil.tracio.service.EmailService;
import com.dvil.tracio.util.AuthenValidation;
import com.dvil.tracio.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class AuthenServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenServiceImpl.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserRepo userRepository;
    private final AuthenValidation verifyUserRequest;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authManager;

    public AuthenServiceImpl(UserRepo userRepository, AuthenValidation verifyUserRequest,
                             UserMapper userMapper, JwtService jwtService, EmailService emailService, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.verifyUserRequest = verifyUserRequest;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.authManager = authManager;
    }


    @Override
    public RegisterResponse Register(RegisterRequest request) {
        verifyUserRequest.ValidRegister(request);

        // Lấy danh sách role hoặc mặc định CYCLIST
        List<UserRole> userRoles = new ArrayList<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            userRoles.add(new UserRole(null, RoleName.CYCLIST)); // Role mặc định
        } else {
            for (RoleName role : request.getRoles()) {
                if (isAllowedRole(role)) {
                    userRoles.add(new UserRole(null, role));
                } else {
                    throw new IllegalArgumentException("Role không hợp lệ: " + role);
                }
            }
        }

        // Tạo User mới
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setUserPassword(encoder.encode(request.getPassword()));
        newUser.setPhone(request.getPhone());
        newUser.setCreatedAt(Instant.now());
        newUser.setAccountStatus(UserVerifyStatus.Unverified);
        newUser.setUserRoles(userRoles);

        // Liên kết UserRole với User
        for (UserRole userRole : userRoles) {
            userRole.setUser(newUser);
        }

        // Tạo JWT Token
        String accessToken = jwtService.generateAccessToken(newUser);
        newUser.setAccessToken(accessToken);
        String refreshToken = jwtService.generateRefreshToken(newUser);
        newUser.setRefToken(refreshToken);

        // Lưu user vào DB
        userRepository.save(newUser);

        // Gửi email xác thực
        String verifyCode = generateVerificationCode();
        emailService.sendVerifyCode(newUser, verifyCode, "Xác thực tài khoản", "Mã xác thực của bạn là: ");

        // Convert User -> DTO
        UserDTO userDTO = userMapper.toDTO(newUser);
        return new RegisterResponse("Đăng ký thành công!", userDTO, accessToken, refreshToken);
    }

    @Override
    public String authenticate(LoginRequest request) throws Exception {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu"));

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (auth.isAuthenticated()) {
                return jwtService.generateAccessToken(user);
            }
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Xác thực thất bại");
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Mã 6 chữ số
    }

    private boolean isAllowedRole(RoleName role) {
        return role == RoleName.CYCLIST || role == RoleName.SHOP_OWNER;
    }
}
