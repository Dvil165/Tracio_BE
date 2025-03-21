package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.BlogDTO;
import com.dvil.tracio.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "createdBy.username", target = "createdByUsername")
    BlogDTO toDTO(Blog blog);

    @Mapping(source = "createdByUserId", target = "createdBy.id")
    Blog toEntity(BlogDTO blogDTO);
}

