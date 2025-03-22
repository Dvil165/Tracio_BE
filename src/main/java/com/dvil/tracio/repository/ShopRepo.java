package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepo extends JpaRepository<Shop, Integer> {
    List<Shop> findByOwnerId(Integer ownerId);
    Shop findByShpName(String name);
}
