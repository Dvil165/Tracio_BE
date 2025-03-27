package com.dvil.tracio.repository;

import com.dvil.tracio.entity.ShopService;
import com.dvil.tracio.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopServiceRepo extends JpaRepository<ShopService, Integer> {
    List<ShopService> findByShopId(Integer shopId);
    List<ShopService> findByServiceId(Integer serviceId);
    boolean existsByShopIdAndService_ServName(Integer serviceId, ServiceType type);

}
