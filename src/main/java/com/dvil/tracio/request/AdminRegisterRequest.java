package com.dvil.tracio.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class AdminRegisterRequest {
    private String password;
    private String email;
    private String phone;
}
