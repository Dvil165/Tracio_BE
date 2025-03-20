package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserPassword(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getAccountStatus(),
                user.getRole() // Chỉ lấy 1 role duy nhất thay vì danh sách roles
        );
    }
}
