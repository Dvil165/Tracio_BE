package com.dvil.tracio.service;

import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.request.ResetPasswordRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.response.ResetPasswordResponse;

public interface AuthenticationService {
    RegisterResponse Register(RegisterRequest request) throws Exception;
    LoginResponse authenticate(LoginRequest request) throws Exception;
    ResetPasswordResponse ResetPassword(ResetPasswordRequest request) throws Exception;
}
