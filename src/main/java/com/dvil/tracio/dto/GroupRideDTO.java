package com.dvil.tracio.dto;

import com.dvil.tracio.enums.MatchStatus;
import com.dvil.tracio.enums.MatchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class GroupRideDTO {
    private Integer id;
    private Integer createdByUserId;
    private OffsetDateTime startTime;
    private OffsetDateTime finishTime;
    private String startPoint;
    private String endPoint;
    private Integer routeId;
    private String location;
    private MatchStatus matchStatus;
    private MatchType matchType;

    private String matchPassword;
}
