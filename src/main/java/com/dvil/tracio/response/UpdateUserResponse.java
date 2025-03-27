package com.dvil.tracio.response;

import com.dvil.tracio.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserResponse {
    private String message;
    private User user;
}
