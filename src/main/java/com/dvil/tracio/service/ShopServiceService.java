package com.dvil.tracio.service;

import com.dvil.tracio.dto.ShopServiceDTO;
import java.util.List;

public interface ShopServiceService {
    List<ShopServiceDTO> getAllShopServices();
    List<ShopServiceDTO> getShopServicesByShopId(Integer shopId);
    List<ShopServiceDTO> getShopServicesByServiceId(Integer serviceId);
    ShopServiceDTO getShopServiceById(Integer id);
    ShopServiceDTO createShopService(ShopServiceDTO shopServiceDTO);
    void deleteShopService(Integer id);
}
