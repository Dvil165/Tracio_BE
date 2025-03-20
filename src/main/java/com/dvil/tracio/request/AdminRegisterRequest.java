package com.dvil.tracio.request;

import lombok.Data;
import lombok.Getter;

@Data
public class AdminRegisterRequest {
    private String password;
    private String email;
    private String phone;
}
