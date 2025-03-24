package com.dvil.tracio.dto;

import com.dvil.tracio.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

public record ShopRequestDTO(
        Integer id,
        Integer sentBy,
        RequestStatus status,
        String processedBy
) {
}
