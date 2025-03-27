package com.dvil.tracio.controller;

import com.dvil.tracio.dto.GroupRideDTO;
import com.dvil.tracio.service.GroupRideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group-rides")
public class GroupRideController {
    private final GroupRideService groupRideService;

    public GroupRideController(GroupRideService groupRideService) {
        this.groupRideService = groupRideService;
    }

    @GetMapping
    public ResponseEntity<List<GroupRideDTO>> getAllGroupRides() {
        return ResponseEntity.ok(groupRideService.getAllGroupRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRideDTO> getGroupRideById(@PathVariable Integer id) {
        return ResponseEntity.ok(groupRideService.getGroupRideById(id));
    }

    @GetMapping("/my-group")
    public ResponseEntity<List<GroupRideDTO>> getMyGroupRides() {
        return ResponseEntity.ok(groupRideService.getMyGroupRides());
    }

    // 🚀 Tạo mới GroupRide (PRIVATE cần mật khẩu)
    @PostMapping
    public ResponseEntity<?> createGroupRide(@RequestBody GroupRideDTO groupRideDTO) {
        try {
            GroupRideDTO createdGroupRide = groupRideService.createGroupRide(groupRideDTO);
            return ResponseEntity.ok(Map.of("message", "GroupRide đã được tạo thành công!", "groupRide", createdGroupRide));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🚀 Cập nhật GroupRide
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupRide(@PathVariable Integer id, @RequestBody GroupRideDTO groupRideDTO) {
        try {
            GroupRideDTO updatedGroupRide = groupRideService.updateGroupRide(id, groupRideDTO);
            return ResponseEntity.ok(Map.of("message", "Cập nhật GroupRide thành công!", "groupRide", updatedGroupRide));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        }
    }


    // 🚀 Xóa GroupRide
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupRide(@PathVariable Integer id) {
        try {
            groupRideService.deleteGroupRide(id);
            return ResponseEntity.ok(Map.of("message", "GroupRide với ID " + id + " đã bị xóa thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
