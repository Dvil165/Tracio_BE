package com.dvil.tracio.mapper;

import com.dvil.tracio.dto.ProductDTO;
import com.dvil.tracio.entity.Product;
import com.dvil.tracio.entity.ProductImage;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class ProductMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getProductType(),
                product.getCreatedAt(),
                product.getShop().getId(),
                product.getProductImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList()

        );
    }

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.productName());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setProductType(productDTO.type()); // Enum
        return product;
    }
}
