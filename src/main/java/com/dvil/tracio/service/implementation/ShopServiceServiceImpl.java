package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ShopServiceDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.Srvice;
import com.dvil.tracio.entity.ShopService;
import com.dvil.tracio.mapper.ShopServiceMapper;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.repository.SrviceRepo;
import com.dvil.tracio.repository.ShopServiceRepo;
import com.dvil.tracio.service.ShopServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceServiceImpl implements ShopServiceService {
    private final ShopServiceRepo shopServiceRepo;
    private final ShopRepo shopRepo;
    private final SrviceRepo srviceRepo;
    private final ShopServiceMapper shopServiceMapper = ShopServiceMapper.INSTANCE;

    public ShopServiceServiceImpl(ShopServiceRepo shopServiceRepo, ShopRepo shopRepo, SrviceRepo srviceRepo) {
        this.shopServiceRepo = shopServiceRepo;
        this.shopRepo = shopRepo;
        this.srviceRepo = srviceRepo;
    }

    @Override
    public List<ShopServiceDTO> getAllShopServices() {
        List<ShopServiceDTO> shopServices = shopServiceRepo.findAll().stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());

        if (shopServices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có dịch vụ nào được đăng ký bởi cửa hàng");
        }
        return shopServices;
    }

    @Override
    public List<ShopServiceDTO> getShopServicesByShopId(Integer shopId) {
        List<ShopServiceDTO> shopServices = shopServiceRepo.findByShopId(shopId).stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());

        if (shopServices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có dịch vụ nào được đăng ký bởi cửa hàng có ID " + shopId);
        }
        return shopServices;
    }

    @Override
    public List<ShopServiceDTO> getShopServicesByServiceId(Integer serviceId) {
        List<ShopServiceDTO> shopServices = shopServiceRepo.findByServiceId(serviceId).stream()
                .map(shopServiceMapper::toDTO)
                .collect(Collectors.toList());

        if (shopServices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có cửa hàng nào đăng ký dịch vụ có ID " + serviceId);
        }
        return shopServices;
    }

    @Override
    public ShopServiceDTO getShopServiceById(Integer id) {
        ShopService shopService = shopServiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ của cửa hàng với ID " + id + " không tồn tại"));

        return shopServiceMapper.toDTO(shopService);
    }

    @Override
    @Transactional
    public ShopServiceDTO createShopService(ShopServiceDTO shopServiceDTO) {
        Shop shop = shopRepo.findById(shopServiceDTO.getShopId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + shopServiceDTO.getShopId() + " không tồn tại"));

        Srvice srvice = srviceRepo.findById(shopServiceDTO.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + shopServiceDTO.getServiceId() + " không tồn tại"));

        ShopService shopService = shopServiceMapper.toEntity(shopServiceDTO);
        shopService.setShop(shop);
        shopService.setService(srvice);
        shopService.setCreatedAt(OffsetDateTime.now());

        shopService = shopServiceRepo.save(shopService);
        return shopServiceMapper.toDTO(shopService);
    }

    @Override
    @Transactional
    public void deleteShopService(Integer id) {
        ShopService shopService = shopServiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ của cửa hàng với ID " + id + " không tồn tại"));

        shopServiceRepo.delete(shopService);
    }
}
