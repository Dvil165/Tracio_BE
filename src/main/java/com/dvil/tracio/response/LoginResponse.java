package com.dvil.tracio.response;

import com.dvil.tracio.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
    private RoleName role;
}
