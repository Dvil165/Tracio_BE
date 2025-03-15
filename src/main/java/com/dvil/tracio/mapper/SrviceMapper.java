package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Srvice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SrviceMapper {
    SrviceMapper INSTANCE = Mappers.getMapper(SrviceMapper.class);

    SrviceDTO toDTO(Srvice srvice);

    Srvice toEntity(SrviceDTO srviceDTO);
}
