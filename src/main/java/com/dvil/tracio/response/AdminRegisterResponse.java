package com.dvil.tracio.response;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class AdminRegisterResponse {
    private String msg;
    private String username;
    private String password;
}
