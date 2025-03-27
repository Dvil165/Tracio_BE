package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.entity.Order;
import com.dvil.tracio.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderMapper implements Function<Order, OrderDTO> {
    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getShop().getId(),
                order.getStaff().getUsername(),
                order.getStatus(),
                order.getTotalPrice()
        );
    }
}
