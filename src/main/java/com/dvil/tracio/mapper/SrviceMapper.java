package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Srvice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SrviceMapper {
    SrviceMapper INSTANCE = Mappers.getMapper(SrviceMapper.class);

    @Mapping(target = "servType", source = "servName")
    SrviceDTO toDTO(Srvice srvice);

    @Mapping(target = "servName", source = "servType")
    Srvice toEntity(SrviceDTO srviceDTO);
}
