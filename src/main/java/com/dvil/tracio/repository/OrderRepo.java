package com.dvil.tracio.repository;

import com.dvil.tracio.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
    Integer countTotalOrders();

    @Query(value = "SELECT COUNT(*) FROM orders WHERE shop_id = :shopId", nativeQuery = true)
    Integer countOrdersByShopId(@Param("shopId") Integer shopId);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE staff_id = :staffId", nativeQuery = true)
    Integer countOrdersByStaffId(@Param("staffId") Integer staffId);

    @Query(value = """
    SELECT u.id FROM users u
    LEFT JOIN orders o ON u.id = o.staff_id
    WHERE u.role = 'STAFF' AND u.shop_id = :shopId
    GROUP BY u.id
    ORDER BY COUNT(o.id) ASC
    LIMIT 1
    """, nativeQuery = true)
    Integer findLeastBusyStaff(@Param("shopId") Integer shopId);
}
