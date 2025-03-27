package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.request.UpdateUserInforRequest;
import com.dvil.tracio.response.UpdateUserResponse;
import com.dvil.tracio.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class UserServiceImplemented implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplemented.class);
    private final UserRepo userRepository;
    private final UserMapper userMapper;

    public UserServiceImplemented(UserRepo userRepository,
                                  UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getEmployeesByShop(Integer shopId) {
        return userRepository.findByShopId(shopId);
    }

    @Override
    public UpdateUserResponse updateUser(Integer userid,
                                         UpdateUserInforRequest request) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getUsername()));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        User updatedUser = userRepository.save(user);
        return new UpdateUserResponse("User successfully updated", updatedUser);
    }


}

