package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.ShopService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepo shopRepo;
    private final UserRepo userRepo;
    private final ShopMapper shopMapper;

    public ShopServiceImpl(ShopRepo shopRepo, UserRepo userRepo, ShopMapper shopMapper) {
        this.shopRepo = shopRepo;
        this.userRepo = userRepo;
        this.shopMapper = shopMapper;
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
                .map(shopMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopDTO> getShopsByOwnerId(Integer ownerId) {
        return shopRepo.findByOwnerId(ownerId).stream()
                .map(shopMapper::apply)
                .collect(Collectors.toList());
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
