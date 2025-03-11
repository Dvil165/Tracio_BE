package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMap {
    public UserDTO maptoUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getUserRole(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getAccountStatus(),
                user.getAccessToken(),
                user.getRefToken()
        );
    }

    public User maptoUserEntity(UserDTO dto) {
        return new User(
                dto.getId(),
                dto.getAcc_token(),
                dto.getRef_token(),
                dto.getAccountStatus(),
                dto.getCreatedAt(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getUserPassword(),
                dto.getUserRole(),
                dto.getUsername()
        );
    }
}
