package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userPassword", ignore = true) // Không nên trả về mật khẩu
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
