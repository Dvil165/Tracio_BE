package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.entity.Product;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.mapper.ProductMapper;
import com.dvil.tracio.repository.ProductRepo;
import com.dvil.tracio.repository.ShopRepo;
import com.dvil.tracio.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ShopRepo shopRepo;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public ProductServiceImpl(ProductRepo productRepo, ShopRepo shopRepo) {
        this.productRepo = productRepo;
        this.shopRepo = shopRepo;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = productRepo.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có sản phẩm nào trong hệ thống");
        }
        return products;
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm với ID " + id + " không tồn tại"));

        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Shop shop = shopRepo.findById(productDTO.getShopId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cửa hàng với ID " + productDTO.getShopId() + " không tồn tại"));

        Product product = productMapper.toEntity(productDTO);
        product.setShop(shop);
        product.setCreatedAt(OffsetDateTime.now());

        product = productRepo.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm với ID " + id + " không tồn tại"));

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setType(productDTO.getType());

        return productMapper.toDTO(productRepo.save(existingProduct));
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm với ID " + id + " không tồn tại"));

        productRepo.delete(product);
    }
}
