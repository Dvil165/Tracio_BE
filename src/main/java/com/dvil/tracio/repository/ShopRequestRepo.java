package com.dvil.tracio.repository;

import com.dvil.tracio.entity.ShopRequest;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRequestRepo extends JpaRepository<ShopRequest, Integer> {
    boolean existsByShopName(String shopname);
    boolean existsByUserAndStatus(User user, RequestStatus status);
}
