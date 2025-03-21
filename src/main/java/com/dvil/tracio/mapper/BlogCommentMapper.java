package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.BlogCommentDTO;
import com.dvil.tracio.entity.BlogComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlogCommentMapper {
    BlogCommentMapper INSTANCE = Mappers.getMapper(BlogCommentMapper.class);

    @Mapping(source = "blog.id", target = "blogId")
    @Mapping(source = "user.id", target = "userId")
    BlogCommentDTO toDTO(BlogComment blogComment);

    @Mapping(source = "blogId", target = "blog.id")
    @Mapping(source = "userId", target = "user.id")
    BlogComment toEntity(BlogCommentDTO blogCommentDTO);
}
