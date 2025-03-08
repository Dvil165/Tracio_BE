package com.dvil.tracio.service.implementation;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.UserService;
import com.dvil.tracio.util.Authentication;
import com.dvil.tracio.util.JwtService;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;

@Service
public class UserServiceImplemented implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplemented.class);
    private final UserRepo userRepository;
    private final Authentication verifyUserRequest;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserServiceImplemented(UserRepo userRepository, Authentication verifyUserRequest, UserMapper userMapper, JwtService jwtService) {
        this.userRepository = userRepository;
        this.verifyUserRequest = verifyUserRequest;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public RegisterResponse Register(RegisterRequest request) {
        verifyUserRequest.ValidRegister(request);
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setUserPassword(request.getPassword());
            user.setUserRole(RoleName.CYCLIST);
            user.setPhone(request.getPhone());
            user.setCreatedAt(Instant.now());
            user.setAccountStatus(UserVerifyStatus.Unverified);
            user.setAccessToken(jwtService.generateAccessToken(user));
            UserDTO dto = userMapper.toDTO(userRepository.save(user));
            return new RegisterResponse("Đăng ký thành công!", dto);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lỗi khi lưu dữ liệu vào database!");
        }
    }

    @Override
    public LoginResponse Login(LoginRequest request) throws Exception {
        return null;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
