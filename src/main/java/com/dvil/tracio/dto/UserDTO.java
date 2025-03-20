package com.dvil.tracio.dto;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;

import java.time.Instant;
import java.util.List;

public record UserDTO(
        Integer id,
        String username,
        String email,
        String userPassword,
        String phone,
        Instant createdAt,
        UserVerifyStatus accountStatus,
        RoleName role // Đổi từ RoleName thành List<RoleName>
) {
}
