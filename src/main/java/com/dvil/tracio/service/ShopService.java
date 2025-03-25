package com.dvil.tracio.service;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.request.CreateEmployeeRequest;
import com.dvil.tracio.response.RegisterResponse;
import org.apache.catalina.User;

import java.util.List;

public interface ShopService {
    String createShop(ShopDTO shopDTO, Integer ownerId);

    ShopDTO getShopById(Integer id);

    List<ShopDTO> getAllShops();

    List<ShopDTO> getShopsByOwnerId(Integer ownerId);

    ShopDTO updateShop(Integer id, ShopDTO shopDTO);

    String deleteShop(Integer id);

    RegisterResponse createEmployee(Integer shopId, CreateEmployeeRequest request);
}
