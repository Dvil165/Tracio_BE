package com.dvil.tracio.service.implementation;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.UserDTOMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.EmailService;
import com.dvil.tracio.service.UserService;
import com.dvil.tracio.util.AuthenValidation;
import com.dvil.tracio.util.JwtService;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;
import javax.crypto.SecretKey;
import java.util.stream.Collectors;

@Service
public class UserServiceImplemented implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplemented.class);
    private final UserRepo userRepository;
    private final AuthenValidation verifyUserRequest;
    private final UserDTOMapper userDTOMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImp userDetailsService;
    private final EmailService emailService;

    public UserServiceImplemented(UserRepo userRepository, AuthenValidation verifyUserRequest, UserDTOMapper userDTOMapper,
                                  JwtService jwtService, PasswordEncoder passwordEncoder,
                                  AuthenticationManager authenticationManager, UserDetailsServiceImp userDetailsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.verifyUserRequest = verifyUserRequest;
        this.userDTOMapper = userDTOMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    public RegisterResponse Register(RegisterRequest request) {
        verifyUserRequest.ValidRegister(request);
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setUserPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserRole(RoleName.CYCLIST);
            user.setPhone(request.getPhone());
            user.setCreatedAt(Instant.now());
            user.setAccountStatus(UserVerifyStatus.Unverified);
            user.setAccessToken(jwtService.generateAccessToken(user));
            user.setRefToken(jwtService.generateRefreshToken(user));
            SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS384);
            String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
            logger.info(base64Key);
//            logger.info(dto.getAcc_token());
//            logger.info(dto.getRef_token());
            userRepository.save(user);
            emailService.sendVerifyCode(user, "12345", "TEST MAIL", "Ur code: ");
            return new RegisterResponse("Đăng ký thành công!", userDTOMapper.apply(user));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lỗi khi lưu dữ liệu vào database!");
        }
    }

    @Override
    public LoginResponse Login(LoginRequest request) throws Exception {
        try {
            logger.info("Before authenticationManager.authenticate");
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            logger.info("Encoded password from DB: {}", user.getUserPassword());
            logger.info("authenticationManager: {}", authenticationManager);
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("After authenticationManager.authenticate");
            // Lưu thông tin authentication vào SecurityContext
            //SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin user từ database
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            logger.info("access token generated for user: {}", user.getUsername());
            user.setAccessToken(accessToken);
            user.setRefToken(refreshToken);
            userRepository.save(user);
            Date expiration = jwtService.extractExpiration(accessToken);
            return new LoginResponse(accessToken, refreshToken, user.getUserRole());

        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username or password");
        }
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream().map(userMapper::maptoUserDTO)
//                .collect(Collectors.toList());
//    }
}
