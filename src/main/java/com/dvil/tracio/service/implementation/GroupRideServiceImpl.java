package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.GroupRideDTO;
import com.dvil.tracio.entity.GroupRide;
import com.dvil.tracio.entity.GroupRideJoiner;
import com.dvil.tracio.entity.Route;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.MatchType;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.GroupRideMapper;
import com.dvil.tracio.repository.GroupRideJoinerRepo;
import com.dvil.tracio.repository.GroupRideRepo;
import com.dvil.tracio.repository.RouteRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.GroupRideService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRideServiceImpl implements GroupRideService {
    private final GroupRideRepo groupRideRepo;
    private final RouteRepo routeRepo;
    private final UserRepo userRepo;
    private final GroupRideJoinerRepo groupRideJoinerRepo;
    private final GroupRideMapper groupRideMapper = GroupRideMapper.INSTANCE;

    public GroupRideServiceImpl(
            GroupRideRepo groupRideRepo,
            RouteRepo routeRepo,
            UserRepo userRepo,
            GroupRideJoinerRepo groupRideJoinerRepo
    ) {
        this.groupRideRepo = groupRideRepo;
        this.routeRepo = routeRepo;
        this.userRepo = userRepo;
        this.groupRideJoinerRepo = groupRideJoinerRepo;
    }

    @Override
    public List<GroupRideDTO> getAllGroupRides() {
        List<GroupRideDTO> groupRides = groupRideRepo.findAll().stream().map(groupRide -> {
            GroupRideDTO dto = groupRideMapper.toDTO(groupRide);
            dto.setMatchPassword(null); // Không trả về mật khẩu trong danh sách
            return dto;
        }).collect(Collectors.toList());

        if (groupRides.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có GroupRide nào trong hệ thống!");
        }

        return groupRides;
    }

    @Override
    public GroupRideDTO getGroupRideById(Integer id) {
        GroupRide groupRide = groupRideRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupRide không tồn tại!"));

        GroupRideDTO dto = groupRideMapper.toDTO(groupRide);
        dto.setMatchPassword(null); // Không trả về mật khẩu trong chi tiết group ride
        return dto;
    }

    @Override
    public List<GroupRideDTO> getMyCreatedGroupRides() {
        User currentUser = getCurrentUser();
        List<GroupRide> myGroupRides = groupRideRepo.findByCreatedBy_Id(currentUser.getId());

        if (myGroupRides.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bạn chưa tạo GroupRide nào.");
        }

        return myGroupRides.stream()
                .map(groupRide -> {
                    GroupRideDTO dto = groupRideMapper.toDTO(groupRide);
                    dto.setMatchPassword(null); // Ẩn mật khẩu
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroupRideDTO
    createGroupRide(GroupRideDTO groupRideDTO) {
        User user = getCurrentUser();
        Route route = routeRepo.findById(groupRideDTO.getRouteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Route"));

        boolean isAdminOrCyclist = user.getRole().equals(RoleName.ADMIN) || user.getRole().equals(RoleName.CYCLIST);
        if (!isAdminOrCyclist) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo GroupRide");
        }

        GroupRide groupRide = groupRideMapper.toEntity(groupRideDTO);
        groupRide.setCreatedBy(user);
        groupRide.setRoute(route);

        if (groupRideDTO.getMatchType() == MatchType.PRIVATE) {
            if (groupRideDTO.getMatchPassword() == null || groupRideDTO.getMatchPassword().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GroupRide PRIVATE cần có mật khẩu");
            }
            groupRide.setMatchPassword(groupRideDTO.getMatchPassword());
        }

        groupRide = groupRideRepo.save(groupRide);

        GroupRideJoiner joiner = new GroupRideJoiner();
        joiner.setUser(user);
        joiner.setGroupRide(groupRide);
        groupRideJoinerRepo.save(joiner);

        return groupRideMapper.toDTO(groupRide);
    }

    @Override
    @Transactional
    public GroupRideDTO updateGroupRide(Integer id, GroupRideDTO groupRideDTO) {
        User user = getCurrentUser();

        GroupRide existingGroupRide = groupRideRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupRide với ID " + id + " không tồn tại"));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = existingGroupRide.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật GroupRide này");
        }

        // Nếu chuyển từ OPEN sang PRIVATE thì phải có mật khẩu
        if (existingGroupRide.getMatchType() == MatchType.OPEN
                && groupRideDTO.getMatchType() == MatchType.PRIVATE
                && (groupRideDTO.getMatchPassword() == null || groupRideDTO.getMatchPassword().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cần nhập mật khẩu khi chuyển từ OPEN sang PRIVATE");
        }

        // Partial update
        if (groupRideDTO.getMatchType() != null) {
            existingGroupRide.setMatchType(groupRideDTO.getMatchType());
        }

        if (groupRideDTO.getMatchPassword() != null && !groupRideDTO.getMatchPassword().isBlank()) {
            existingGroupRide.setMatchPassword(groupRideDTO.getMatchPassword());
        }

        if (groupRideDTO.getStartTime() != null) {
            existingGroupRide.setStartTime(groupRideDTO.getStartTime());
        }

        if (groupRideDTO.getFinishTime() != null) {
            existingGroupRide.setFinishTime(groupRideDTO.getFinishTime());
        }

        if (groupRideDTO.getStartPoint() != null && !groupRideDTO.getStartPoint().isBlank()) {
            existingGroupRide.setStartPoint(groupRideDTO.getStartPoint());
        }

        if (groupRideDTO.getEndPoint() != null && !groupRideDTO.getEndPoint().isBlank()) {
            existingGroupRide.setEndPoint(groupRideDTO.getEndPoint());
        }

        if (groupRideDTO.getLocation() != null && !groupRideDTO.getLocation().isBlank()) {
            existingGroupRide.setLocation(groupRideDTO.getLocation());
        }

        if (groupRideDTO.getMatchStatus() != null) {
            existingGroupRide.setMatchStatus(groupRideDTO.getMatchStatus());
        }

        return groupRideMapper.toDTO(groupRideRepo.save(existingGroupRide));
    }


    @Override
    @Transactional
    public void deleteGroupRide(Integer id) {
        User user = getCurrentUser();
        GroupRide groupRide = groupRideRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy GroupRide với ID " + id));
        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = groupRide.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá GroupRide này");
        }
        groupRideRepo.delete(groupRide);
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
