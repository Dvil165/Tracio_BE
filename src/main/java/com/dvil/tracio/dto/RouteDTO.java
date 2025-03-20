package com.dvil.tracio.dto;

import lombok.Data;

@Data
public class RouteDTO {
    private Integer id;
    private Double routeLength;
    private Double estimatedTime;
    private String difficulty;
    private String startLocation;
    private String destination;
    private String location;
}
