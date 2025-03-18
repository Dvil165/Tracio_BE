package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.ShopService;
import org.springframework.http.HttpStatus;
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

    @Override
    public String createShop(ShopDTO shopDTO, Integer ownerId) {
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

        Shop shop = shopMapper.toEntity(shopDTO);
        if (!(owner.getUserRole() == RoleName.SHOP_OWNER)) {
            return "fail";
        }
            shop.setOwner(owner);
            shop.setCreatedAt(OffsetDateTime.now());

            shopRepo.save(shop);
            return "ok";


    }

    @Override
    public ShopDTO getShopById(Integer id) {
        Shop shop = shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + id + " không tồn tại"));
        return shopMapper.toDTO(shop);
    }

    @Override
    public List<ShopDTO> getAllShops() {
        return shopRepo.findAll().stream()
                .map(shopMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopDTO> getShopsByOwnerId(Integer ownerId) {
        return shopRepo.findByOwnerId(ownerId).stream()
                .map(shopMapper::toDTO)
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
        return shopMapper.toDTO(shop);
    }

    @Override
    public String deleteShop(Integer id) {
        Shop shop = shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + id + " không tồn tại"));

        shopRepo.delete(shop);
        return "Cửa hàng với ID " + id + " đã được xóa thành công!";
    }
}
