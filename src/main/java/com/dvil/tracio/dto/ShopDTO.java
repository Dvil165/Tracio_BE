package com.dvil.tracio.dto;

import java.time.OffsetDateTime;

public record ShopDTO(
        String shpName,
        String shpLocation,
        String openHours,
        String shpDescription
) {

}
