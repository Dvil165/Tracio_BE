package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    RouteDTO toDTO(Route route);

    @Mapping(source = "userId", target = "user.id")
    Route toEntity(RouteDTO routeDTO);
}
