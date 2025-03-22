package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.Product;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.repository.UserRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ShopMapper implements Function<Shop, ShopDTO> {
    private final UserRepo userRepo;

    public ShopMapper(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ShopDTO apply(Shop shop) {
        return new ShopDTO(
                shop.getShpName(),
                shop.getShpLocation(),
                shop.getOpenHours(),
                shop.getShpDescription()
        );
    }

    public Shop toEntity(ShopDTO shopDTO, User owner) {
        Shop shop = new Shop();
        shop.setOpenHours(shopDTO.openHours());
        shop.setShpName(shopDTO.shpName());
        shop.setShpLocation(shopDTO.shpLocation());
        shop.setShpDescription(shopDTO.shpDescription());
        shop.setOwner(owner);
        return shop;
    }
}

