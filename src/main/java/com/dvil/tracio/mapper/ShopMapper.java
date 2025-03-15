package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ShopDTO;
import com.dvil.tracio.entity.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    @Mapping(source = "owner.id", target = "ownerId")
    ShopDTO toDTO(Shop shop);

    @Mapping(target = "owner", ignore = true)
    Shop toEntity(ShopDTO shopDTO);
}

