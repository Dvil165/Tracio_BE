package com.dvil.tracio.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEmployeeRequest {
    private String username;
    private String password;
    private String Mail;
}
