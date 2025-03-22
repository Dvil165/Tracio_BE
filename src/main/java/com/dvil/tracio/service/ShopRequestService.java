package com.dvil.tracio.service;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.CreateShopResponse;

public interface ShopRequestService {
    CreateShopResponse createShopRequest(CreateShopRequest request, User owner);
}
