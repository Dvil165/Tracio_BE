package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        List<RoleName> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole())
                .collect(Collectors.toList());

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserPassword(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getAccountStatus(),
                roles // Cập nhật danh sách roles thay vì 1 role duy nhất
        );
    }
}
