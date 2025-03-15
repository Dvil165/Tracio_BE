package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.UserDTO;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.UserMapper;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
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


}

