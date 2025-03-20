package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.RouteDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteDetailMapper {
    RouteDetailMapper INSTANCE = Mappers.getMapper(RouteDetailMapper.class);

    @Mapping(source = "route.id", target = "routeId")
    RouteDetailDTO toDTO(RouteDetail routeDetail);

    @Mapping(source = "routeId", target = "route.id")
    RouteDetail toEntity(RouteDetailDTO routeDetailDTO);
}
