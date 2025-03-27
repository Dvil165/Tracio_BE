package com.dvil.tracio.request;

import lombok.Data;

@Data
public class UpdateUserInforRequest {
    private String username;
    private String phone;
    private String email;
}
