package com.dvil.tracio.service;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.request.LoginRequest;
import com.dvil.tracio.request.RegisterRequest;
import com.dvil.tracio.request.UpdateUserInforRequest;
import com.dvil.tracio.response.LoginResponse;
import com.dvil.tracio.response.RegisterResponse;
import com.dvil.tracio.response.UpdateUserResponse;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    void deleteUserById(Integer id);
    List<User> getEmployeesByShop(Integer shopId);
    UpdateUserResponse updateUser(Integer userid, UpdateUserInforRequest request);
}
