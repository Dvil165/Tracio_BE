package com.dvil.tracio.service;

import com.dvil.tracio.dto.GroupRideJoinerDTO;
import java.util.List;

public interface GroupRideJoinerService {
    GroupRideJoinerDTO joinGroupRide(Integer groupRideId, String password);
    void leaveGroupRide(Integer groupRideId);
    List<GroupRideJoinerDTO> getAllJoiners();
    List<GroupRideJoinerDTO> getJoinersByGroupRideId(Integer groupRideId);

}

