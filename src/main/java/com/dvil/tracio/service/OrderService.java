package com.dvil.tracio.service;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Integer id);
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(Integer id, OrderDTO orderDTO);
    void deleteOrder(Integer id);
    Integer getOrderCountByStaffID(User staff);
    Integer getOrderCountByShopID(Shop shop);
}
