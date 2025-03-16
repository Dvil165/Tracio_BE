package com.dvil.tracio.service;

import com.dvil.tracio.dto.OrderDetailDTO;
import java.util.List;

public interface OrderDetailService {
    List<OrderDetailDTO> getAllOrderDetails();
    List<OrderDetailDTO> getOrderDetailsByOrderId(Integer orderId);
    OrderDetailDTO getOrderDetailById(Integer id);
    OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailDTO updateOrderDetail(Integer id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Integer id);
}
