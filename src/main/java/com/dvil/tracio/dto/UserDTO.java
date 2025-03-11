package com.dvil.tracio.dto;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String userPassword;
    private RoleName userRole;
    private String phone;
    private Instant createdAt;
    private UserVerifyStatus accountStatus;
    private String acc_token;
    private String ref_token;
}
