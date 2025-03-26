package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    //boolean existsByUserIdAndRole(Integer id, RoleName role);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE shop_id = :shopId", nativeQuery = true)
    List<User> findByShopId(@Param("shopId") Integer shopId);

    @Query(value = "SELECT COUNT(u.user_role) FROM users u WHERE u.user_role = :role", nativeQuery = true)
    Integer countByRole(@Param("role") String role);
}
