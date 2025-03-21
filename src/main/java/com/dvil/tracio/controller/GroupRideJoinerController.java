package com.dvil.tracio.controller;

import com.dvil.tracio.dto.GroupRideJoinerDTO;
import com.dvil.tracio.service.GroupRideJoinerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group-ride-joiners")
public class GroupRideJoinerController {
    private final GroupRideJoinerService groupRideJoinerService;

    public GroupRideJoinerController(GroupRideJoinerService groupRideJoinerService) {
        this.groupRideJoinerService = groupRideJoinerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllJoiners() {
        try {
            List<GroupRideJoinerDTO> joiners = groupRideJoinerService.getAllJoiners();
            return ResponseEntity.ok(joiners);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        }
    }
    @GetMapping("/{groupRideId}")
    public ResponseEntity<?> getJoinersByGroupRideId(@PathVariable Integer groupRideId) {
        try {
            List<GroupRideJoinerDTO> joiners = groupRideJoinerService.getJoinersByGroupRideId(groupRideId);
            return ResponseEntity.ok(joiners);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        }
    }


    @PostMapping("/join/{groupRideId}")
    public ResponseEntity<?> joinGroupRide(@PathVariable Integer groupRideId,
                                           @RequestParam(required = false) String password) {
        try {
            GroupRideJoinerDTO joiner = groupRideJoinerService.joinGroupRide(groupRideId, password);
            return ResponseEntity.ok(Map.of("message", "Tham gia GroupRide thành công!", "joiner", joiner));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }


    @DeleteMapping("/leave/{groupRideId}")
    public ResponseEntity<?> leaveGroupRide(@PathVariable Integer groupRideId) {
        try {
            groupRideJoinerService.leaveGroupRide(groupRideId);
            return ResponseEntity.ok(Map.of("message", "Bạn đã rời khỏi GroupRide thành công!"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
