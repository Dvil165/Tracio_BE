package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.CreateEmployeeRequest;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.service.ShopService;
import com.dvil.tracio.util.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepo shopRepo;
    private final UserRepo userRepo;
    private final ShopMapper shopMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ShopServiceImpl(ShopRepo shopRepo, UserRepo userRepo, ShopMapper shopMapper, UserMapper userMapper, JwtService jwtService) {
        this.shopRepo = shopRepo;
        this.userRepo = userRepo;
        this.shopMapper = shopMapper;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }


//    @Override
//    public String createShop(ShopDTO shopDTO) {
//        Shop shop = shopMapper.toEntity(shopDTO);
//        User owner = userRepo.findById(ownerId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
//        if (!(owner.getUserRole() == RoleName.SHOP_OWNER)) {
//            return "fail";
//        }
//            shop.setOwner(owner);
//            shop.setCreatedAt(OffsetDateTime.now());
//
//            shopRepo.save(shop);
//            return "ok";
//
//
//    }

    @Override
    public String createShop(ShopDTO shopDTO, Integer ownerId) {
        return "";
    }

    @Override
    public ShopDTO getShopById(Integer id) {
        Shop shop = shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + id + " không tồn tại"));
        return shopMapper.apply(shop);
    }

    @Override
    public List<ShopDTO> getAllShops() {
        return shopRepo.findAll().stream()
                .map(shopMapper)
                .collect(Collectors.toList());
    }

    @Override
    public ShopDTO getShopsByOwnerId(Integer ownerId) {
        return shopMapper.apply(shopRepo.findByOwnerId(ownerId));
    }

    @Override
    public ShopDTO updateShop(Integer id, ShopDTO shopDTO) {
        Shop shop = shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + id + " không tồn tại"));

        shop.setShpName(shopDTO.shpName());
        shop.setShpLocation(shopDTO.shpLocation());
        shop.setOpenHours(shopDTO.openHours());
        shop.setShpDescription(shopDTO.shpDescription());

        shop = shopRepo.save(shop);
        return shopMapper.apply(shop);
    }

    @Override
    @Transactional
    public String deleteShop(Integer id) {
        User currentUser = getCurrentUser();

        Shop shop = shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + id + " không tồn tại"));

        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwnerOfShop = shop.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfShop) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa cửa hàng này");
        }

        shopRepo.delete(shop);
        return "Cửa hàng với ID " + id + " đã được xóa thành công!";
    }

    @Override
    public RegisterResponse createEmployee(CreateEmployeeRequest request, User owner) {

        Shop shop = shopRepo.findById(owner.getShop().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy shop ID " + owner.getShop().getId()));

//        if (!shop.getOwner().getId().equals(owner.getId())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo nhân viên cho shop này!");
//        }

        if (userRepo.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại!");
        }
        if (userRepo.existsByEmail(request.getMail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng!");
        }


        User newEmployee = new User();
        newEmployee.setUsername(request.getUsername());
        newEmployee.setEmail(request.getMail());
        newEmployee.setUserPassword(encoder.encode(request.getPassword())); // Mã hóa mật khẩu
        newEmployee.setPhone(null);
        newEmployee.setCreatedAt(Instant.now());
        newEmployee.setRole(RoleName.STAFF); // Role mặc định là STAFF
        newEmployee.setShop(shop); // Gán shop cho nhân viên
        newEmployee.setAccountStatus(UserVerifyStatus.Verified); // Mặc định nhân viên đã xác thực

        String accessToken = jwtService.generateAccessToken(newEmployee);
        String refreshToken = jwtService.generateRefreshToken(newEmployee);
        newEmployee.setAccessToken(accessToken);
        newEmployee.setRefToken(refreshToken);

        userRepo.save(newEmployee);

        UserDTO userDTO = userMapper.toDTO(newEmployee);
        return new RegisterResponse("Nhân viên được tạo thành công!", userDTO, accessToken, refreshToken);
    }

    @Override
    public List<UserDTO> getEmployeesByShop(User owner) {
        if (!owner.getRole().equals(RoleName.SHOP_OWNER) || shopRepo.findByOwnerId(owner.getId()) == null) {
            throw new AccessDeniedException("You are not a shop owner");
        }

        Shop shop = shopRepo.findByOwnerId(owner.getId());
        List<User> employees = userRepo.findByShopId(shop.getId());

        return employees.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepo.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không thể xác thực người dùng");
    }


}
