package com.dvil.tracio.dto;

import lombok.Data;
import java.util.List;

@Data
public class RouteDTO {
    private Integer id;
    private String username;
    private Double routeLength;
    private Double estimatedTime;
    private String difficulty;
    private String startLocation;
    private String destination;
    private String location;
    private List<RouteDetailDTO> routeDetails = List.of();
}
