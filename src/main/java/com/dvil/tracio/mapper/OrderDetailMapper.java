package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.OrderDetailDTO;
import com.dvil.tracio.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderDetailDTO toDTO(OrderDetail orderDetail);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "productId", target = "product.id")
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);
}
