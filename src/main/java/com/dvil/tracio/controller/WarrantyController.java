package com.dvil.tracio.controller;

import com.dvil.tracio.dto.WarrantyDTO;
import com.dvil.tracio.service.WarrantyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/api/warranties")
public class WarrantyController {
    private final WarrantyService warrantyService;

    public WarrantyController(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    @PostMapping
    public ResponseEntity<WarrantyDTO> createWarranty(@RequestBody WarrantyDTO warrantyDTO) {
        return ResponseEntity.ok(warrantyService.createWarranty(warrantyDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarrantyDTO> getWarrantyById(@PathVariable Integer id) {
        return ResponseEntity.ok(warrantyService.getWarrantyById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<WarrantyDTO>> getAllWarranties() {
        return ResponseEntity.ok(warrantyService.getAllWarranties());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarrantyDTO> updateWarranty(@PathVariable Integer id, @RequestBody WarrantyDTO warrantyDTO) {
        return ResponseEntity.ok(warrantyService.updateWarranty(id, warrantyDTO));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<WarrantyDTO> deleteWarranty(@PathVariable Integer id) {
//        warrantyService.deleteWarranty(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteWarranty(@PathVariable Integer id) {
        WarrantyDTO deletedWarranty = warrantyService.deleteWarranty(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Warranty deleted successfully!");
        response.put("deletedWarranty", deletedWarranty);
        return ResponseEntity.ok(response);
    }

}
