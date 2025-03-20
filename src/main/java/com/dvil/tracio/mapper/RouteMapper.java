package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    RouteDTO toDTO(Route route);
    Route toEntity(RouteDTO routeDTO);
}
