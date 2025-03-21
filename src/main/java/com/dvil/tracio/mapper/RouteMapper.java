package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.RouteDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    RouteDTO toDTO(Route route);
    Route toEntity(RouteDTO routeDTO);

}
