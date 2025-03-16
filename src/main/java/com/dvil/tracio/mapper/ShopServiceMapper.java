package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ShopServiceDTO;
import com.dvil.tracio.entity.ShopService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShopServiceMapper {
    ShopServiceMapper INSTANCE = Mappers.getMapper(ShopServiceMapper.class);

    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "shop.id", target = "shopId")
    ShopServiceDTO toDTO(ShopService shopService);

    @Mapping(source = "serviceId", target = "service.id")
    @Mapping(source = "shopId", target = "shop.id")
    ShopService toEntity(ShopServiceDTO shopServiceDTO);
}
