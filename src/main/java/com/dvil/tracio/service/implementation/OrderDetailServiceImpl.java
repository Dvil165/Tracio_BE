package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.OrderDetailDTO;
import com.dvil.tracio.entity.Order;
import com.dvil.tracio.entity.OrderDetail;
import com.dvil.tracio.entity.Product;
import com.dvil.tracio.mapper.OrderDetailMapper;
import com.dvil.tracio.repository.OrderDetailRepo;
import com.dvil.tracio.repository.OrderRepo;
import com.dvil.tracio.repository.ProductRepo;
import com.dvil.tracio.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepo orderDetailRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    public OrderDetailServiceImpl(OrderDetailRepo orderDetailRepo, OrderRepo orderRepo, ProductRepo productRepo) {
        this.orderDetailRepo = orderDetailRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<OrderDetailDTO> getAllOrderDetails() {
        List<OrderDetailDTO> orderDetails = orderDetailRepo.findAll().stream()
                .map(orderDetailMapper::toDTO)
                .collect(Collectors.toList());

        if (orderDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có chi tiết đơn hàng nào trong hệ thống");
        }
        return orderDetails;
    }

    @Override
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Integer orderId) {
        List<OrderDetailDTO> orderDetails = orderDetailRepo.findByOrderId(orderId).stream()
                .map(orderDetailMapper::toDTO)
                .collect(Collectors.toList());

        if (orderDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có chi tiết đơn hàng nào cho Order ID " + orderId);
        }
        return orderDetails;
    }

    @Override
    public OrderDetailDTO getOrderDetailById(Integer id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết đơn hàng với ID " + id + " không tồn tại"));

        return orderDetailMapper.toDTO(orderDetail);
    }

    @Override
    @Transactional
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepo.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng với ID " + orderDetailDTO.getOrderId() + " không tồn tại"));

        Product product = productRepo.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm với ID " + orderDetailDTO.getProductId() + " không tồn tại"));

        // Kiểm tra nếu sản phẩm đã có trong order
        OrderDetail existingOrderDetail = orderDetailRepo.findByOrderId(orderDetailDTO.getOrderId()).stream()
                .filter(od -> od.getProduct().getId().equals(orderDetailDTO.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingOrderDetail != null) {
            existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + orderDetailDTO.getQuantity());
            existingOrderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());
            return orderDetailMapper.toDTO(orderDetailRepo.save(existingOrderDetail));
        }

        // Nếu chưa có, tạo mới
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);

        orderDetail = orderDetailRepo.save(orderDetail);
        return orderDetailMapper.toDTO(orderDetail);
    }


    @Override
    @Transactional
    public OrderDetailDTO updateOrderDetail(Integer id, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết đơn hàng với ID " + id + " không tồn tại"));

        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        existingOrderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());

        return orderDetailMapper.toDTO(orderDetailRepo.save(existingOrderDetail));
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Integer id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết đơn hàng với ID " + id + " không tồn tại"));

        orderDetailRepo.delete(orderDetail);
    }
}
