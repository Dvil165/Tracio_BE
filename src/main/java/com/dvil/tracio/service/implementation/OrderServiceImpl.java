package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.Order;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.OrderMapper;
import com.dvil.tracio.repository.OrderRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.OrderRequest;
import com.dvil.tracio.response.OrderResponse;
import com.dvil.tracio.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderServiceImpl(OrderRepo orderRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = orderRepo.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có đơn hàng nào trong hệ thống");
        }
        return orders;
    }

    @Override
    public OrderDTO getOrderById(Integer id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng với ID " + id + " không tồn tại"));

        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        return null;
    }

//    @Override
//    @Transactional
//    public OrderDTO createOrder(OrderDTO orderDTO, User user) {
//        Integer staffId = orderRepo.findLeastBusyStaff(shopId);
//        Order order = orderMapper.toEntity(orderDTO);
//        order.setUser(user);
//        order.setOrderDate(OffsetDateTime.now());
//        order = orderRepo.save(order);
//        return orderMapper.toDTO(order);
//    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {
        Order existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng với ID " + id + " không tồn tại"));

        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setTotalPrice(orderDTO.getTotalPrice());

        return orderMapper.toDTO(orderRepo.save(existingOrder));
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng với ID " + id + " không tồn tại"));

        orderRepo.delete(order);
    }

    @Override
    public Integer getOrderCountByStaffID(User staff) {
        return orderRepo.countOrdersByStaffId(staff.getId());
    }

    @Override
    public Integer getOrderCountByShopID(Shop shop) {
        return orderRepo.countOrdersByShopId(shop.getId());
    }
}
