package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.RouteDetail;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.RouteDetailMapper;
import com.dvil.tracio.repository.RouteDetailRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.RouteDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {
    private final RouteDetailRepo routeDetailRepo;
    private final UserRepo userRepo;
    private final RouteDetailMapper routeDetailMapper = RouteDetailMapper.INSTANCE;

    public RouteDetailServiceImpl(RouteDetailRepo routeDetailRepo, UserRepo userRepo) {
        this.routeDetailRepo = routeDetailRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<RouteDetailDTO> getAllRouteDetails() {
        return routeDetailRepo.findAll().stream()
                .map(routeDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteDetailDTO getRouteDetailById(Integer id) {
        RouteDetail routeDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RouteDetail không tồn tại"));
        return routeDetailMapper.toDTO(routeDetail);
    }

    @Override
    @Transactional
    public RouteDetailDTO createRouteDetail(RouteDetailDTO routeDetailDTO, Integer userId) {
        User user = getUserById(userId);

        if (user.getUserRole() != RoleName.CYCLIST && user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo RouteDetail");
        }

        RouteDetail routeDetail = routeDetailMapper.toEntity(routeDetailDTO);
        return routeDetailMapper.toDTO(routeDetailRepo.save(routeDetail));
    }

    @Override
    @Transactional
    public RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO, Integer userId) {
        User user = getUserById(userId);
        RouteDetail existingRouteDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RouteDetail không tồn tại"));

        if (user.getUserRole() != RoleName.CYCLIST && user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật RouteDetail");
        }

        existingRouteDetail.setPathData(routeDetailDTO.getPathData());

        return routeDetailMapper.toDTO(routeDetailRepo.save(existingRouteDetail));
    }

    @Override
    @Transactional
    public void deleteRouteDetail(Integer id, Integer userId) {
        User user = getUserById(userId);

        if (user.getUserRole() != RoleName.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa RouteDetail");
        }

        routeDetailRepo.deleteById(id);
    }

    private User getUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
    }
}
