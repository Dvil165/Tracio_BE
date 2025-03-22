package com.dvil.tracio.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ShopServiceDTO {
    private Integer id;
    private Integer serviceId;
    private Integer shopId;
    private OffsetDateTime createdAt;
}
