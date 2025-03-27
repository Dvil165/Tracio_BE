package com.dvil.tracio.controller;

import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

//    @PostMapping
//    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
//        try {
//            ProductDTO createdProduct = productService.createProduct(productDTO);
//            return ResponseEntity.ok(Map.of("message", "Sản phẩm đã được tạo thành công!", "product", createdProduct));
//        } catch (ResponseStatusException ex) {
//            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
//        }
//    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Sản phẩm đã được tạo thành công!",
                    "product", createdProduct
            ));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(Map.of("error", ex.getReason())); // Sử dụng "error" thay vì "message" để phân biệt lỗi
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định!"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(Map.of("message", "Cập nhật sản phẩm thành công!", "product", updatedProduct));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Sản phẩm với ID " + id + " đã bị xóa thành công!"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", Objects.requireNonNull(ex.getReason())));
        }
    }
}
