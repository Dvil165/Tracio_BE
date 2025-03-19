package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.RouteDetail;
import com.dvil.tracio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteDetailMapper {
    RouteDetailMapper INSTANCE = Mappers.getMapper(RouteDetailMapper.class);

    @Mapping(source = "route.id", target = "routeId")
    @Mapping(source = "user.id", target = "userId")
    RouteDetailDTO toDTO(RouteDetail routeDetail);

    @Mapping(source = "routeId", target = "route", qualifiedByName = "mapRoute")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    RouteDetail toEntity(RouteDetailDTO routeDetailDTO);

    @Named("mapRoute")
    static Route mapRoute(Integer routeId) {
        if (routeId == null) return null;
        Route route = new Route();
        route.setId(routeId);
        return route;
    }

    @Named("mapUser")
    static User mapUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
