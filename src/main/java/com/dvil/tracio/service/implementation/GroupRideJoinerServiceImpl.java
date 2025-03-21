package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.GroupRideJoinerDTO;
import com.dvil.tracio.entity.GroupRide;
import com.dvil.tracio.entity.GroupRideJoiner;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.MatchType;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.GroupRideJoinerMapper;
import com.dvil.tracio.repository.GroupRideJoinerRepo;
import com.dvil.tracio.repository.GroupRideRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.GroupRideJoinerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRideJoinerServiceImpl implements GroupRideJoinerService {
    private final GroupRideJoinerRepo groupRideJoinerRepo;
    private final GroupRideRepo groupRideRepo;
    private final UserRepo userRepo;
    private final GroupRideJoinerMapper groupRideJoinerMapper = GroupRideJoinerMapper.INSTANCE;

    public GroupRideJoinerServiceImpl(GroupRideJoinerRepo groupRideJoinerRepo, GroupRideRepo groupRideRepo, UserRepo userRepo) {
        this.groupRideJoinerRepo = groupRideJoinerRepo;
        this.groupRideRepo = groupRideRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public GroupRideJoinerDTO joinGroupRide(Integer groupRideId, String password) {
        User user = getCurrentUser();
        GroupRide groupRide = groupRideRepo.findById(groupRideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupRide không tồn tại!"));

        // 🔥 Kiểm tra nếu GroupRide là PRIVATE và mật khẩu không khớp
        if (groupRide.getMatchType() == MatchType.PRIVATE) {
            if (password == null || !password.equals(groupRide.getMatchPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mật khẩu không chính xác!");
            }
        }

        // 🔥 Kiểm tra nếu người dùng đã tham gia GroupRide này
        boolean alreadyJoined = groupRideJoinerRepo.existsByGroupRideAndUser(groupRide, user);
        if (alreadyJoined) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn đã tham gia GroupRide này!");
        }

        // ✅ Thêm user vào GroupRide
        GroupRideJoiner joiner = new GroupRideJoiner();
        joiner.setGroupRide(groupRide);
        joiner.setUser(user);

        groupRideJoinerRepo.save(joiner);

        return groupRideJoinerMapper.toDTO(joiner);
    }

    @Override
    @Transactional
    public void leaveGroupRide(Integer groupRideId) {
        User user = getCurrentUser();
        GroupRide groupRide = groupRideRepo.findById(groupRideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupRide không tồn tại!"));

        GroupRideJoiner joiner = groupRideJoinerRepo.findByGroupRideAndUser(groupRide, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn chưa tham gia GroupRide này!"));

        groupRideJoinerRepo.delete(joiner);
    }

    @Override
    public List<GroupRideJoinerDTO> getAllJoiners() {
        User user = getCurrentUser();

        // 🔥 Nếu là Admin, trả về toàn bộ danh sách GroupRideJoiners
        if (user.getRole().equals(RoleName.ADMIN)) {
            return groupRideJoinerRepo.findAll()
                    .stream()
                    .map(groupRideJoinerMapper::toDTO)
                    .collect(Collectors.toList());
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem danh sách này.");
    }

    @Override
    public List<GroupRideJoinerDTO> getJoinersByGroupRideId(Integer groupRideId) {
        User user = getCurrentUser();

        GroupRide groupRide = groupRideRepo.findById(groupRideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupRide với ID " + groupRideId + " không tồn tại"));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isMember = groupRideJoinerRepo.existsByGroupRideAndUser(groupRide, user);

        if (!isAdmin && !isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem danh sách thành viên của GroupRide này.");
        }

        return groupRideJoinerRepo.findByGroupRide(groupRide)
                .stream()
                .map(groupRideJoinerMapper::toDTO)
                .collect(Collectors.toList());
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
