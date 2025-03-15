package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.WarrantyDTO;
import com.dvil.tracio.entity.Product;
import com.dvil.tracio.entity.Warranty;
import com.dvil.tracio.mapper.WarrantyMapper;
import com.dvil.tracio.repository.ProductRepo;
import com.dvil.tracio.repository.WarrantyRepo;
import com.dvil.tracio.service.WarrantyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarrantyServiceImpl implements WarrantyService {
    private final WarrantyRepo warrantyRepo;
    private final ProductRepo productRepo;
    private final WarrantyMapper warrantyMapper;

    public WarrantyServiceImpl(WarrantyRepo warrantyRepo, ProductRepo productRepo, WarrantyMapper warrantyMapper) {
        this.warrantyRepo = warrantyRepo;
        this.productRepo = productRepo;
        this.warrantyMapper = warrantyMapper;
    }

    @Override
    public WarrantyDTO createWarranty(WarrantyDTO warrantyDTO) {
        Optional<Product> product = productRepo.findById(warrantyDTO.getProductId());
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại");
        }
        Warranty warranty = warrantyMapper.toEntity(warrantyDTO);
        warranty.setProduct(product.get());
        return warrantyMapper.toDTO(warrantyRepo.save(warranty));
    }

    @Override
    public WarrantyDTO getWarrantyById(Integer id) {
        Warranty warranty = warrantyRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bảo hành không tồn tại"));
        return warrantyMapper.toDTO(warranty);
    }

    @Override
    public List<WarrantyDTO> getAllWarranties() {
        return warrantyRepo.findAll()
                .stream()
                .map(warrantyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WarrantyDTO updateWarranty(Integer id, WarrantyDTO warrantyDTO) {
        Warranty existingWarranty = warrantyRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bảo hành không tồn tại"));

        Optional<Product> product = productRepo.findById(warrantyDTO.getProductId());
        if (product.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại");
        }

        existingWarranty.setWarrantyPeriod(warrantyDTO.getWarrantyPeriod());
        existingWarranty.setWarrantyTerms(warrantyDTO.getWarrantyTerms());
        existingWarranty.setProduct(product.get());

        return warrantyMapper.toDTO(warrantyRepo.save(existingWarranty));
    }

//    @Override
//    public void deleteWarranty(Integer id) {
//        if (!warrantyRepo.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bảo hành không tồn tại");
//        }
//        warrantyRepo.deleteById(id);
//    }

    @Override
    @Transactional
    public WarrantyDTO deleteWarranty(Integer id) {
        Warranty warranty = warrantyRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warranty not found with id: " + id));

        // Xóa liên kết với Product trước khi xóa
        Product product = warranty.getProduct();
        if (product != null) {
            product.setWarranty(null);
        }
        warrantyRepo.delete(warranty);
        return warrantyMapper.toDTO(warranty);
    }

}
