package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    @Mapping(source = "createdBy", target = "createdByUserId", qualifiedByName = "userToUserId")
    RouteDTO toDTO(Route route);

    @Mapping(source = "createdByUserId", target = "createdBy", qualifiedByName = "userIdToUser")
    Route toEntity(RouteDTO routeDTO);

    @Named("userToUserId")
    static Integer userToUserId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("userIdToUser")
    static User userIdToUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
