package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.request.ResetPasswordRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.response.ResetPasswordResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;


@Service
public class AuthenServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenServiceImpl.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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
        // Tạo User mới
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setUserPassword(encoder.encode(request.getPassword()));
        newUser.setPhone(request.getPhone());
        newUser.setCreatedAt(Instant.now());
        newUser.setAccountStatus(UserVerifyStatus.Unverified);

        newUser.setRole(request.getRole() == null ? RoleName.CYCLIST : request.getRole());
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
    public LoginResponse authenticate(LoginRequest request)
            throws BadCredentialsException, ResponseStatusException {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Optional<User> optionalUser  = userRepository.findByUsername(request.getUsername());
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }
            User user = optionalUser.get();
            if (jwtService.isTokenExpired(user.getAccessToken())) {
                if (!jwtService.isTokenExpired(user.getRefToken())) {
                    // Chỉ tạo mới Access Token từ Refresh Token
                    String newAccessToken = jwtService.generateAccessToken(user);
                    user.setAccessToken(newAccessToken);
                    userRepository.save(user);
                } else {
                    // Cả hai token hết hạn -> tạo mới cả Access & Refresh Token
                    String newAccessToken = jwtService.generateAccessToken(user);
                    String newRefreshToken = jwtService.generateRefreshToken(user);
                    user.setAccessToken(newAccessToken);
                    user.setRefToken(newRefreshToken);
                    userRepository.save(user);
                }
            }
            return new LoginResponse(user.getUsername(), user.getEmail(), user.getAccessToken()
                                   , user.getRefToken(), user.getRole());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai tên đăng nhập hoặc mật khẩu");
        }
    }

    @Override
    @Transactional
    public ResetPasswordResponse ResetPassword(ResetPasswordRequest request) throws Exception {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            emailService.sendVerifyCode(user, generateVerificationCode()
                    , "Reset password: ", "Mã xác thực của bạn là: ");
            return new ResetPasswordResponse("Sent");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trong quá trình gửi email", e);
        }
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Mã 6 chữ số
    }

}
