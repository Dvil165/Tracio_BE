package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.mapper.RouteMapper;
import com.dvil.tracio.repository.RouteRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepo routeRepo;
    private final UserRepo userRepo; // Cần để kiểm tra quyền người dùng
    private final RouteMapper routeMapper = RouteMapper.INSTANCE;

    public RouteServiceImpl(RouteRepo routeRepo, UserRepo userRepo) {
        this.routeRepo = routeRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<RouteDTO> getAllRoutes() {
        List<RouteDTO> routes = routeRepo.findAll().stream()
                .map(routeMapper::toDTO)
                .collect(Collectors.toList());

        if (routes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có lộ trình nào trong hệ thống");
        }
        return routes;
    }

    @Override
    public RouteDTO getRouteById(Integer id) {
        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        return routeMapper.toDTO(route);
    }

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) {
        User user = getCurrentUser();
        if (!userHasRole(user, "ADMIN") && !userHasRole(user, "CYCLIST")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo lộ trình");
        }

        Route route = routeMapper.toEntity(routeDTO);
        route.setUsername(user.getUsername());
        route = routeRepo.save(route);
        return routeMapper.toDTO(route);
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Integer id, RouteDTO routeDTO) {
        User user = getCurrentUser();
        Route existingRoute = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        if (!userHasRole(user, "ADMIN") || userHasRole(user, "CYCLIST")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật lộ trình");
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
    public void deleteRoute(Integer id) {
        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));
        routeRepo.delete(route);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepo.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không thể xác thực người dùng");
    }

    private boolean userHasRole(User user, String role) {
        return user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
