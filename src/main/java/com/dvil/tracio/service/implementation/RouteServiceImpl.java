package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.RouteMapper;
import com.dvil.tracio.repository.RouteRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepo routeRepo;
    private final UserRepo userRepo;
    private final RouteMapper routeMapper = RouteMapper.INSTANCE;

    public RouteServiceImpl(RouteRepo routeRepo, UserRepo userRepo) {
        this.routeRepo = routeRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<RouteDTO> getAllRoutes() {
        return routeRepo.findAll().stream()
                .map(routeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteDTO getRouteById(Integer id) {
        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tuyến đường không tồn tại"));
        return routeMapper.toDTO(route);
    }

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO, Integer userId) {
        User user = getUserById(userId);

        if (user.getUserRole() != RoleName.CYCLIST && user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo tuyến đường");
        }

        Route route = routeMapper.toEntity(routeDTO);
        route.setCreatedBy(user);

        return routeMapper.toDTO(routeRepo.save(route));
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Integer id, RouteDTO routeDTO, Integer userId) {
        User user = getUserById(userId);
        Route existingRoute = getRouteByIdOrThrow(id);

        if (user.getUserRole() != RoleName.CYCLIST && user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật tuyến đường");
        }

        existingRoute.setRouteLength(routeDTO.getRouteLength());
        existingRoute.setEstimatedTime(routeDTO.getEstimatedTime());
        existingRoute.setDifficulty(routeDTO.getDifficulty());
        existingRoute.setStartLocation(routeDTO.getStartLocation());
        existingRoute.setDestination(routeDTO.getDestination());
        existingRoute.setLocation(routeDTO.getLocation());

        return routeMapper.toDTO(routeRepo.save(existingRoute));
    }

    @Override
    @Transactional
    public void deleteRoute(Integer id, Integer userId) {
        User user = getUserById(userId);

        if (user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa tuyến đường");
        }

        Route route = getRouteByIdOrThrow(id);
        routeRepo.delete(route);
    }

    private User getUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
    }

    private Route getRouteByIdOrThrow(Integer id) {
        return routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tuyến đường không tồn tại"));
    }
}
