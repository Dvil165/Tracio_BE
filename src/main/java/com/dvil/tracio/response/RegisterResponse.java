package com.dvil.tracio.response;

import com.dvil.tracio.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private UserDTO user;
    private String accessToken;
    private String refreshToken;
}
