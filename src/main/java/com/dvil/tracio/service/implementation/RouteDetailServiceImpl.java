package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.RouteDetail;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.repository.RouteRepo;
import com.dvil.tracio.repository.RouteDetailRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.RouteDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {
    private final RouteRepo routeRepo;
    private final RouteDetailRepo routeDetailRepo;
    private final UserRepo userRepo;

    public RouteDetailServiceImpl(RouteRepo routeRepo, RouteDetailRepo routeDetailRepo, UserRepo userRepo) {
        this.routeRepo = routeRepo;
        this.routeDetailRepo = routeDetailRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public RouteDetailDTO addRouteDetail(RouteDetailDTO routeDetailDTO) {
        Route route = routeRepo.findById(routeDetailDTO.getRouteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Route với ID " + routeDetailDTO.getRouteId() + " không tồn tại"
                ));

        User currentUser = getCurrentUser();

        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwner = route.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền thêm RouteDetail vào route này"
            );
        }

        RouteDetail routeDetail = new RouteDetail();
        routeDetail.setPathData(routeDetailDTO.getPathData());
        routeDetail.setRoute(route);

        routeDetail = routeDetailRepo.save(routeDetail);

        return mapToDTO(routeDetail);
    }

    @Override
    public List<RouteDetailDTO> getRouteDetailsByRouteId(Integer routeId) {
        List<RouteDetail> details = routeDetailRepo.findByRouteId(routeId);
        if (details.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có RouteDetail nào cho Route ID: " + routeId);
        }
        return details.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

//    @Override
//    public RouteDetailDTO getRouteDetailById(Integer id) {
//        RouteDetail routeDetail = routeDetailRepo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RouteDetail với ID " + id + " không tồn tại"));
//        return mapToDTO(routeDetail);
//    }

    @Override
    @Transactional
    public RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO) {
        RouteDetail routeDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RouteDetail với ID " + id + " không tồn tại"));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwner = routeDetail.getRoute().getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật RouteDetail này");
        }

        routeDetail.setPathData(routeDetailDTO.getPathData());
        routeDetail = routeDetailRepo.save(routeDetail);

        return mapToDTO(routeDetail);
    }


    @Override
    @Transactional
    public void deleteRouteDetail(Integer id) {
        RouteDetail routeDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RouteDetail với ID " + id + " không tồn tại"));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole().equals(RoleName.ADMIN);
        boolean isOwner = routeDetail.getRoute().getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá RouteDetail này");
        }

        routeDetailRepo.delete(routeDetail);
    }

    private RouteDetailDTO mapToDTO(RouteDetail routeDetail) {
        RouteDetailDTO dto = new RouteDetailDTO();
        dto.setId(routeDetail.getId());
        dto.setPathData(routeDetail.getPathData());
        dto.setRouteId(routeDetail.getRoute().getId());
        return dto;
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
