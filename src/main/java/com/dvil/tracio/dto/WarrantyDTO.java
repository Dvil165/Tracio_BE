package com.dvil.tracio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyDTO {
    private Integer id;
    private OffsetDateTime warrantyPeriod;
    private String warrantyTerms;
    private Integer productId;
}
