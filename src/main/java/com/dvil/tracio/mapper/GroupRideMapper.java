package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.GroupRideDTO;
import com.dvil.tracio.entity.GroupRide;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GroupRideMapper {
    GroupRideMapper INSTANCE = Mappers.getMapper(GroupRideMapper.class);

    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "route.id", target = "routeId")
    @Mapping(target = "matchPassword", source = "matchPassword")
    GroupRideDTO toDTO(GroupRide groupRide);

    @Mapping(source = "createdByUserId", target = "createdBy.id")
    @Mapping(source = "routeId", target = "route.id")
    @Mapping(target = "matchPassword", source = "matchPassword")
    GroupRide toEntity(GroupRideDTO groupRideDTO);
}
