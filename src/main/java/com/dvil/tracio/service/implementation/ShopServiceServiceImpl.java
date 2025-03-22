package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopServiceDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.ShopService;
import com.dvil.tracio.entity.Srvice;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.ShopServiceMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.ShopServiceRepo;
import com.dvil.tracio.repository.SrviceRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.ShopServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceServiceImpl implements ShopServiceService {
    private final ShopServiceRepo shopServiceRepo;
    private final ShopRepo shopRepo;
    private final SrviceRepo srviceRepo;
    private final UserRepo userRepo;
    private final ShopServiceMapper shopServiceMapper = ShopServiceMapper.INSTANCE;

    public ShopServiceServiceImpl(ShopServiceRepo shopServiceRepo, ShopRepo shopRepo, SrviceRepo srviceRepo, UserRepo userRepo) {
        this.shopServiceRepo = shopServiceRepo;
        this.shopRepo = shopRepo;
        this.srviceRepo = srviceRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<ShopServiceDTO> getAllShopServices() {
        return shopServiceRepo.findAll().stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopServiceDTO> getShopServicesByShopId(Integer shopId) {
        return shopServiceRepo.findByShopId(shopId).stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopServiceDTO> getShopServicesByServiceId(Integer serviceId) {
        return shopServiceRepo.findByServiceId(serviceId).stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShopServiceDTO getShopServiceById(Integer id) {
        ShopService shopService = shopServiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ShopService với ID " + id));
        return shopServiceMapper.toDTO(shopService);
    }

//    @Override
//    @Transactional
//    public ShopServiceDTO createShopService(Integer serviceId) {
//        User user = getCurrentUser();
//
//        if (!user.getRole().equals(RoleName.SHOP_OWNER)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ Shop Owner mới có quyền đăng ký dịch vụ");
//        }
//
//        Shop shop = shopRepo.findByOwnerId(user.getId()).stream()
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy shop của bạn"));
//
//        Srvice srvice = srviceRepo.findById(serviceId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ không tồn tại"));
//
//        ShopService shopService = new ShopService();
//        shopService.setShop(shop);
//        shopService.setService(srvice);
//        shopService.setCreatedAt(OffsetDateTime.now());
//
//        return shopServiceMapper.toDTO(shopServiceRepo.save(shopService));
//    }

    @Override
    @Transactional
    public void deleteShopService(Integer id) {
        ShopService shopService = shopServiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ của shop với ID " + id));
        shopServiceRepo.delete(shopService);
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
