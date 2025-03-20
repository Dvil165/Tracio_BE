package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.RouteDetailDTO;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.RouteDetail;
import com.dvil.tracio.mapper.RouteDetailMapper;
import com.dvil.tracio.repository.RouteDetailRepo;
import com.dvil.tracio.repository.RouteRepo;
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
    private final RouteRepo routeRepo;
    private final RouteDetailMapper routeDetailMapper = RouteDetailMapper.INSTANCE;

    public RouteDetailServiceImpl(RouteDetailRepo routeDetailRepo, RouteRepo routeRepo) {
        this.routeDetailRepo = routeDetailRepo;
        this.routeRepo = routeRepo;
    }

    @Override
    public List<RouteDetailDTO> getAllRouteDetails() {
        List<RouteDetailDTO> routeDetails = routeDetailRepo.findAll().stream()
                .map(routeDetailMapper::toDTO)
                .collect(Collectors.toList());

        if (routeDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có chi tiết lộ trình nào trong hệ thống");
        }
        return routeDetails;
    }

    @Override
    public List<RouteDetailDTO> getRouteDetailsByRouteId(Integer routeId) {
        List<RouteDetailDTO> routeDetails = routeDetailRepo.findByRouteId(routeId).stream()
                .map(routeDetailMapper::toDTO)
                .collect(Collectors.toList());

        if (routeDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có chi tiết nào cho lộ trình với ID " + routeId);
        }
        return routeDetails;
    }

    @Override
    public RouteDetailDTO getRouteDetailById(Integer id) {
        RouteDetail routeDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết lộ trình với ID " + id + " không tồn tại"));

        return routeDetailMapper.toDTO(routeDetail);
    }

    @Override
    @Transactional
    public RouteDetailDTO createRouteDetail(RouteDetailDTO routeDetailDTO) {
        Route route = routeRepo.findById(routeDetailDTO.getRouteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lộ trình với ID " + routeDetailDTO.getRouteId() + " không tồn tại"));

        RouteDetail routeDetail = routeDetailMapper.toEntity(routeDetailDTO);
        routeDetail.setRoute(route);
        routeDetail = routeDetailRepo.save(routeDetail);

        return routeDetailMapper.toDTO(routeDetail);
    }

    @Override
    @Transactional
    public RouteDetailDTO updateRouteDetail(Integer id, RouteDetailDTO routeDetailDTO) {
        RouteDetail existingRouteDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết lộ trình với ID " + id + " không tồn tại"));

        existingRouteDetail.setPathData(routeDetailDTO.getPathData());

        return routeDetailMapper.toDTO(routeDetailRepo.save(existingRouteDetail));
    }

    @Override
    @Transactional
    public void deleteRouteDetail(Integer id) {
        RouteDetail routeDetail = routeDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi tiết lộ trình với ID " + id + " không tồn tại"));

        routeDetailRepo.delete(routeDetail);
    }
}
