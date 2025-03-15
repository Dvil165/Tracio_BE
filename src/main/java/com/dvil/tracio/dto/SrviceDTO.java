package com.dvil.tracio.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SrviceDTO {
    private Integer id;
    private String servName;
    private String servDescription;
    private OffsetDateTime createdAt;
}
