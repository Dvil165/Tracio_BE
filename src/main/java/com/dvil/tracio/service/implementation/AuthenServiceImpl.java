package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
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
        // xử lí input của user
        verifyUserRequest.ValidRegister(request);
        // Tạo User mới
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setUserPassword(encoder.encode(request.getPassword()));
        user.setUserRole(RoleName.CYCLIST);
        user.setPhone(request.getPhone());
        user.setCreatedAt(Instant.now());
        user.setAccountStatus(UserVerifyStatus.Unverified);

        // Tạo JWT Token
        String accessToken = jwtService.generateAccessToken(user);
        user.setAccessToken(accessToken);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefToken(refreshToken);
        userRepository.save(user);
        // Gửi email xác thực
        String verifyCode = generateVerificationCode();
        emailService.sendVerifyCode(user, verifyCode, "Xác thực tài khoản", "Mã xác thực của bạn là: ");

        // Convert User -> DTO
        UserDTO userDTO = userMapper.toDTO(user);
        return new RegisterResponse("Đăng ký thành công!", userDTO, accessToken, refreshToken);
    }

    @Override
    public String authenticate(LoginRequest request) throws Exception {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
//        logger.info(user.getAccessToken());
//        Date expirationDate = jwtService.extractExpiration(user.getAccessToken());
//        logger.info("Token expiration: " + expirationDate);
//        logger.info("Current time: " + new Date());
//        logger.info("Token expired? " + jwtService.isTokenExpired(user.getAccessToken()));
//        if (jwtService.isTokenExpired(user.getAccessToken())){
//            user.setAccessToken(jwtService.generateAccessToken(user));
//            userRepository.save(user);
//        }
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(auth.isAuthenticated())
            return "success";

        return "fail";
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Mã 6 chữ số
    }
}
