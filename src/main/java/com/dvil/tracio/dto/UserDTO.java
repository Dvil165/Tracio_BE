package com.dvil.tracio.dto;

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
    private String userRole;
    private String phone;
    private Instant createdAt;
    private String accountStatus;
    private String acc_token;
    private String ref_token;
    private String fuck_token;
}
