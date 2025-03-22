package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.ShopRequest;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RequestStatus;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.ShopRequestRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.response.CreateShopResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.AdminService;
import com.dvil.tracio.service.EmailService;
import com.dvil.tracio.util.AuthenValidation;
import com.dvil.tracio.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ShopRepo shopRepository;
    private final ShopMapper shopMapper;
    private final ShopRequestRepo shopRequestRepo;

    public AdminServiceImpl(UserRepo userRepository, JwtService jwtService, EmailService emailService
                          , ShopRepo shopRepository, ShopMapper shopMapper, ShopRequestRepo shopRequestRepo) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.shopRequestRepo = shopRequestRepo;
    }


//    @Override
//    public CreateShopResponse handleCreateShopRequest(CreateShopRequest request, Integer ownerId) {
//        if (shopRequestRepo.existsByShopName(request.getShop().shpName())) {
//            return new CreateShopResponse("Shop name is already taken");
//        }
//
//        // Kiểm tra xem user đã có request chờ duyệt chưa
//        if (shopRequestRepo.existsByUserAndStatus(userRepository.getReferenceById(ownerId), RequestStatus.PENDING)) {
//            return new CreateShopResponse("You already have a pending request");
//        }
//
//        User owner = userRepository.findById(ownerId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        ShopRequest shopRequest = new ShopRequest();
//        shopRequest.setUser(owner);
//        shopRequest.setShopName(request.getShop().shpName());
//        shopRequest.setStatus(RequestStatus.PENDING);
//        shopRequestRepo.save(shopRequest);
//        return new CreateShopResponse("Shop request submitted, waiting for approval");
//    }

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
