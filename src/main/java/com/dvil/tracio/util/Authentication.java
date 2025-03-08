package com.dvil.tracio.util;


import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class Authentication {
    private final UserRepo userRepository;

    public Authentication(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public void ValidRegister(RegisterRequest request) {
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại!");
        } else if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu xác nhận không khớp!");
        }
    }
}
