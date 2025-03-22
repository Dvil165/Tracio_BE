package com.dvil.tracio.service.implementation;

import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.ShopRequest;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RequestStatus;
import com.dvil.tracio.mapper.ShopMapper;
import com.dvil.tracio.repository.ShopRequestRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.CreateShopRequest;
import com.dvil.tracio.response.CreateShopResponse;
import com.dvil.tracio.service.ShopRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ShopRequestServiceImpl implements ShopRequestService {

    private final ShopRequestRepo shopRequestRepo;
    private final UserRepo userRepo;
    private final ShopMapper shopMapper;

    public ShopRequestServiceImpl(ShopRequestRepo shopRequestRepo, UserRepo userRepo, ShopMapper shopMapper) {
        this.shopRequestRepo = shopRequestRepo;
        this.userRepo = userRepo;
        this.shopMapper = shopMapper;
    }

    @Override
    public CreateShopResponse createShopRequest(CreateShopRequest request, User owner) {
        Shop shop = shopMapper.toEntity(request.getShop(), owner);
        if (shopRequestRepo.existsByShopName(shop.getShpName())) {
            return new CreateShopResponse("Shop name is already taken");
        }
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setUser(owner);
        shopRequest.setShopName(request.getShop().shpName());
        shopRequest.setStatus(RequestStatus.PENDING);
        shopRequestRepo.save(shopRequest);
        return new CreateShopResponse("Shop request submitted, waiting for approval");
    }
}
