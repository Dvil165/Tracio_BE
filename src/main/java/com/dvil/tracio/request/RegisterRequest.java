package com.dvil.tracio.request;

import com.dvil.tracio.enums.RoleName;
import lombok.Data;


@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private RoleName role;
}
