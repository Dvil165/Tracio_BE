package com.dvil.tracio.repository;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.Srvice;
import com.dvil.tracio.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SrviceRepo extends JpaRepository<Srvice, Integer> {
    //Optional<Srvice> findByServNameAndServDescription(String servName, String servDescription);
    @Query(value = "SELECT * FROM Srvices s WHERE shop_id = :shopId", nativeQuery = true)
    List<Srvice> findAllByShopId(@Param("shopId") Integer shopId);

    boolean existsByServNameAndShop_Id(ServiceType servName, Integer shopId);
}
