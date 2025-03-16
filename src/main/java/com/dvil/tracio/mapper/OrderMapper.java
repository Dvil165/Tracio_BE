package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.OrderDTO;
import com.dvil.tracio.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "user.id", target = "userId")
    OrderDTO toDTO(Order order);

    @Mapping(source = "userId", target = "user.id")
    Order toEntity(OrderDTO orderDTO);
}
