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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        return routeMapper.toDTO(route);
    }
    @Override
    public List<RouteDTO> getMyRoutes() {
        User currentUser = getCurrentUser();
        List<Route> myRoutes = routeRepo.findByUserId(currentUser.getId());

        if (myRoutes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bạn chưa tạo lộ trình nào");
        }

        return myRoutes.stream()
                .map(routeMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) {
        User user = getCurrentUser();

        if (!(user.getRole().equals(RoleName.ADMIN) || user.getRole().equals(RoleName.CYCLIST))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo lộ trình");
        }

        Route route = routeMapper.toEntity(routeDTO);
        route.setUser(user); // ✅ Gán đúng user hiện tại
        route.setUsername(user.getUsername());
        Route savedRoute = routeRepo.save(route);
        return routeMapper.toDTO(savedRoute);
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(Integer id, RouteDTO routeDTO) {
        User currentUser = getCurrentUser();

        Route existingRoute = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwner = existingRoute.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật lộ trình này");
        }

        if (routeDTO.getRouteLength() != null) {
            existingRoute.setRouteLength(routeDTO.getRouteLength());
        }

        if (routeDTO.getEstimatedTime() != null) {
            existingRoute.setEstimatedTime(routeDTO.getEstimatedTime());
        }

        if (routeDTO.getDifficulty() != null && !routeDTO.getDifficulty().trim().isEmpty()) {
            existingRoute.setDifficulty(routeDTO.getDifficulty());
        }

        if (routeDTO.getStartLocation() != null && !routeDTO.getStartLocation().trim().isEmpty()) {
            existingRoute.setStartLocation(routeDTO.getStartLocation());
        }

        if (routeDTO.getDestination() != null && !routeDTO.getDestination().trim().isEmpty()) {
            existingRoute.setDestination(routeDTO.getDestination());
        }

        if (routeDTO.getLocation() != null && !routeDTO.getLocation().trim().isEmpty()) {
            existingRoute.setLocation(routeDTO.getLocation());
        }

        Route updatedRoute = routeRepo.save(existingRoute);
        return routeMapper.toDTO(updatedRoute);
    }


    @Override
    @Transactional
    public void deleteRoute(Integer id) {
        User currentUser = getCurrentUser();

        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lộ trình với ID " + id + " không tồn tại"));

        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwner = route.getUser().getId().equals(currentUser.getId());

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
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không thể xác thực người dùng");
    }
}
