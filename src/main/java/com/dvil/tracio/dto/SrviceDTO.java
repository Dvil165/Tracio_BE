package com.dvil.tracio.dto;

import com.dvil.tracio.enums.ServiceType;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SrviceDTO {
    private Integer id;
    private ServiceType servType;
    private String servDescription;
}
