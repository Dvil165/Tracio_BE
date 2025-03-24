package com.dvil.tracio.service;

import com.dvil.tracio.dto.ShopRequestDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.AdminRegisterResponse;
import com.dvil.tracio.response.CreateShopResponse;

import java.util.List;

public interface AdminService {
    //CreateShopResponse handleCreateShopRequest(CreateShopRequest request, Integer owner_id);
    AdminRegisterResponse createAdminAccount(AdminRegisterRequest adminRegisterRequest);
    List<ShopRequestDTO> getAllRequest();
    String rejectShopRequest(Integer requestId, User admin);
    String approveShopRequest(Integer requestId, User admin);
}
