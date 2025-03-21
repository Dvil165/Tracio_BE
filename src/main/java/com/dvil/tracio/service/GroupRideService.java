package com.dvil.tracio.service;

import com.dvil.tracio.dto.GroupRideDTO;
import java.util.List;

public interface GroupRideService {
    List<GroupRideDTO> getAllGroupRides();
    GroupRideDTO getGroupRideById(Integer id);
    GroupRideDTO createGroupRide(GroupRideDTO groupRideDTO);
    GroupRideDTO updateGroupRide(Integer id, GroupRideDTO groupRideDTO);
    void deleteGroupRide(Integer id);
}
