package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.*;
import com.dvil.tracio.enums.OrderStatus;
import com.dvil.tracio.mapper.OrderMapper;
import com.dvil.tracio.repository.*;
import com.dvil.tracio.request.OrderDetailRequest;
import com.dvil.tracio.request.OrderRequest;
import com.dvil.tracio.response.OrderResponse;
import com.dvil.tracio.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final ShopRepo shopRepo;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepo productRepo;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderServiceImpl(OrderRepo orderRepo, UserRepo userRepo, ShopRepo shopRepo, OrderDetailRepository orderDetailRepository, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.shopRepo = shopRepo;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepo = productRepo;
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
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. Lấy thông tin User và Shop
        User user = userRepo.findById(orderRequest.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Shop shop = shopRepo.findById(orderRequest.getShopId())
                .orElseThrow(() -> new UsernameNotFoundException("Shop not found"));

        // 2. Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setShop(shop);
        order.setOrderDate(OffsetDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setTotalPrice(orderRequest.getTotalPrice());
        Integer staffID = orderRepo.findLeastBusyStaff(orderRequest.getShopId());
        User staff = userRepo.findById(staffID)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        order.setStaff(staff);
        orderRepo.save(order); // Lưu vào DB để có ID

        // 3. Tạo danh sách OrderDetail
        for (OrderDetailRequest item : orderRequest.getItems()) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getPrice()); // Lưu giá tại thời điểm mua

            orderDetailRepository.save(orderDetail);
        }

        return new OrderResponse(new OrderDTO(user.getId(), shop.getId(),
                                              staff.getUsername(), order.getStatus(), order.getTotalPrice()));
    }


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
