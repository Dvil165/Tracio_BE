package com.dvil.tracio.service;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;

import java.util.List;

public interface UserService {
    RegisterResponse Register(RegisterRequest request) throws Exception;

    LoginResponse Login(LoginRequest request) throws Exception;

    List<UserDTO> getAllUsers();
}
