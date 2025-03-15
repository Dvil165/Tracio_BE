package com.dvil.tracio.service;

import com.dvil.tracio.dto.ShopDTO;
import java.util.List;

public interface ShopService {
    ShopDTO createShop(ShopDTO shopDTO, Integer ownerId);

    ShopDTO getShopById(Integer id);

    List<ShopDTO> getAllShops();

    List<ShopDTO> getShopsByOwnerId(Integer ownerId);

    ShopDTO updateShop(Integer id, ShopDTO shopDTO);

    String deleteShop(Integer id);
}
