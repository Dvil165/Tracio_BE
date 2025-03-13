package com.dvil.tracio.service;

import com.dvil.tracio.dto.WarrantyDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WarrantyService {
    WarrantyDTO createWarranty(WarrantyDTO warrantyDTO);
    WarrantyDTO getWarrantyById(Integer id);
    List<WarrantyDTO> getAllWarranties();
    WarrantyDTO updateWarranty(Integer id, WarrantyDTO warrantyDTO);
    ResponseEntity<String> deleteWarranty(Integer id);
}
