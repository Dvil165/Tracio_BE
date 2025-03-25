package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ShopRequestDTO;
import com.dvil.tracio.entity.ShopRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ShopRequestMapper implements Function<ShopRequest, ShopRequestDTO> {
    @Override
    public ShopRequestDTO apply(ShopRequest shopRequest) {
        return new ShopRequestDTO(
                shopRequest.getId(),
                shopRequest.getUser().getId(),
                shopRequest.getStatus(),
                shopRequest.getProcessedBy() != null ? shopRequest.getProcessedBy().getUsername() : null
        );
    }
}
