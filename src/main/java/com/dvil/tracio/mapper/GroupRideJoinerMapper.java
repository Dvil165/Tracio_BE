package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.GroupRideJoinerDTO;
import com.dvil.tracio.entity.GroupRideJoiner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GroupRideJoinerMapper {
    GroupRideJoinerMapper INSTANCE = Mappers.getMapper(GroupRideJoinerMapper.class);

    @Mapping(source = "groupRide.id", target = "groupRideId")
    @Mapping(source = "user.id", target = "userId")
    GroupRideJoinerDTO toDTO(GroupRideJoiner groupRideJoiner);
}
