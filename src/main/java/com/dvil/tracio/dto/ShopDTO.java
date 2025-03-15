package com.dvil.tracio.dto;

import java.time.OffsetDateTime;

public record ShopDTO(
        Integer id,
        String shpName,
        String shpLocation,
        String openHours,
        String shpDescription,
        OffsetDateTime createdAt,
        Integer ownerId
) {}
