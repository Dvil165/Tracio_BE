package com.dvil.tracio.request;

import com.dvil.tracio.dto.ShopDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateShopRequest {
    ShopDTO shop;
}
