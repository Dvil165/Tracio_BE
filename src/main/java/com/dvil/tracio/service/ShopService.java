package com.dvil.tracio.service;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.request.CreateEmployeeRequest;
import com.dvil.tracio.response.RegisterResponse;

import java.util.List;

public interface ShopService {
    String createShop(ShopDTO shopDTO, Integer ownerId);

    ShopDTO getShopById(Integer id);

    List<ShopDTO> getAllShops();

    ShopDTO getShopsByOwnerId(Integer ownerId);

    ShopDTO updateShop(Integer id, ShopDTO shopDTO);

    String deleteShop(Integer id);

    RegisterResponse createEmployee(Integer shopId, CreateEmployeeRequest request, User owner);

    List<UserDTO> getEmployeesByShop(Integer shopId, User owner);
}
