package com.dvil.tracio.service;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.Order;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.request.OrderRequest;
import com.dvil.tracio.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Integer id);
    OrderResponse createOrder(OrderRequest request);
    OrderDTO updateOrder(Integer id, OrderDTO orderDTO);
    void deleteOrder(Integer id);
    Integer getOrderCountByStaffID(User staff);
    Integer getOrderCountByShopID(Shop shop);
    //List<OrderDTO> getOrdersByStaffId(Integer staffid);
}
