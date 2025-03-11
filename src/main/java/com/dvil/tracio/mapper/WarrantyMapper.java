package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.WarrantyDTO;
import com.dvil.tracio.entity.Warranty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WarrantyMapper {
    WarrantyMapper INSTANCE = Mappers.getMapper(WarrantyMapper.class);

    @Mapping(source = "product.id", target = "productId")
    WarrantyDTO toDTO(Warranty warranty);

    @Mapping(source = "productId", target = "product.id")
    Warranty toEntity(WarrantyDTO warrantyDTO);
}
