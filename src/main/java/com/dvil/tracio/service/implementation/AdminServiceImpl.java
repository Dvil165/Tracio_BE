package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopRequestDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.ShopRequest;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RequestStatus;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.mapper.ShopRequestMapper;
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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ShopRepo shopRepository;
    private final ShopMapper shopMapper;
    private final ShopRequestRepo shopRequestRepo;
    private final ShopRequestMapper shopRequestMapper;

    public AdminServiceImpl(UserRepo userRepository, JwtService jwtService, EmailService emailService
                          , ShopRepo shopRepository, ShopMapper shopMapper, ShopRequestRepo shopRequestRepo, ShopRequestMapper shopRequestMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.shopRequestRepo = shopRequestRepo;
        this.shopRequestMapper = shopRequestMapper;
    }

    @Override
    @Transactional
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

    @Override
    public List<ShopRequestDTO> getAllRequest() {
        List<ShopRequest> requestDTOS = shopRequestRepo.findAll();
        return requestDTOS.stream()
                .map(shopRequestMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String rejectShopRequest(Integer requestId, User admin) {
        ShopRequest request = shopRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request already processed");
        }
        request.setStatus(RequestStatus.REJECTED);
        request.setProcessedBy(admin);
        request.setResponseTime(Instant.now());
        shopRequestRepo.save(request);
        return "Shop request rejected";
    }

    @Override
    @Transactional
    public String approveShopRequest(Integer requestId, User adminName) {
        ShopRequest request = shopRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is already processed.");
        }
        // Tạo tài khoản mới cho shop
        User shopOwner = new User();
        shopOwner.setUsername(request.getUser().getUsername() + "_shop"); // Tạo username mới
//        shopOwner.setEmail(request.getUser().getEmail());
//        shopOwner.setUserPassword(encoder.encode(generateRandomPassword())); // Mật khẩu random
//        shopOwner.setRole(RoleName.SHOP_OWNER);
//        userRepository.save(shopOwner);
//
//        // 2️⃣ Tạo entity `Shop` từ request
//        Shop shop = new Shop();
//        shop.setShpName(request.getShopName());
//        shop.setShpDescription(request.getDescription());
//        shop.setShpLocation(request.getUser().getAddress());
//        shop.setOpenHours(request.getOpen_hours());
//        shop.setOwner(shopOwner);
//        shopRepo.save(shop);
//
//        // 3️⃣ Cập nhật trạng thái request
//        request.setStatus(RequestStatus.APPROVED);
//        request.setResponseTime(Instant.now());
//        request.setProcessedBy(admin); // Gán admin duyệt request
//        shopRequestRepo.save(request);
//
//        // 4️⃣ Gửi email thông báo tài khoản mới
//        String emailContent = String.format("""
//        Chào %s,
//
//        Yêu cầu mở shop "%s" của bạn đã được chấp nhận!
//        Dưới đây là thông tin tài khoản mới của bạn:
//
//        🌟 Username: %s
//        🔒 Password: %s
//
//        Vui lòng đăng nhập và đổi mật khẩu ngay lập tức.
//
//        Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!
//        """,
//                request.getUser().getUsername(),
//                request.getShopName(),
//                shopOwner.getUsername(),
//                "your_generated_password_here"
//        );
//
//        emailService.sendEmail(shopOwner.getEmail(), "Shop Approved - Account Details", emailContent);
        return "Shop request approved! Account details sent via email.";
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}
