package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.RouteMapper;
import com.dvil.tracio.repository.RouteRepo;
import com.dvil.tracio.repository.RouteDetailRepo;
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
    private final RouteDetailRepo routeDetailRepo;
    private final UserRepo userRepo;
    private final RouteMapper routeMapper = RouteMapper.INSTANCE;

    public RouteServiceImpl(RouteRepo routeRepo, RouteDetailRepo routeDetailRepo, UserRepo userRepo) {
        this.routeRepo = routeRepo;
        this.routeDetailRepo = routeDetailRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<RouteDTO> getAllRoutes() {
        List<RouteDTO> routes = routeRepo.findAll().stream()
                .map(routeMapper::toDTO) // 🔥 Thay vì `toDTOWithDetails()`
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

        return routeMapper.toDTO(route); // 🔥 Thay vì `toDTOWithDetails()`
    }

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) {
        User user = getCurrentUser();

        // 🔥 Sửa lại kiểm tra quyền
        boolean hasPermission = user.getRole().equals(RoleName.ADMIN) || user.getRole().equals(RoleName.CYCLIST);

        if (!hasPermission) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo lộ trình");
        }

        Route route = routeMapper.toEntity(routeDTO);
        route.setUsername(user.getUsername());
        final Route savedRoute = routeRepo.save(route);
        return routeMapper.toDTO(savedRoute);
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Integer id, RouteDTO routeDTO) {
        User user = getCurrentUser();
        Route existingRoute = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        // 🔥 Kiểm tra quyền
        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = existingRoute.getUsername().equals(user.getUsername());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật lộ trình này");
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
        User user = getCurrentUser();
        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = route.getUsername().equals(user.getUsername());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá lộ trình này");
        }

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
}
