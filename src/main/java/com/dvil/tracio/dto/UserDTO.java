package com.dvil.tracio.dto;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import java.time.Instant;

public record UserDTO (
     Integer id,
     String username,
     String email,
     String userPassword,
     String phone,
     Instant createdAt,
     UserVerifyStatus accountStatus,
     RoleName userRole
    ){
}
