package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "shop.id", target = "shopId")
    ProductDTO toDTO(Product product);

    @Mapping(source = "shopId", target = "shop.id")
    Product toEntity(ProductDTO productDTO);
}
