package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product addProduct(Product product) {
        List<Error> errors = new ArrayList<>();

        // Validate product name
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            errors.add(Error.PRODUCT_INVALID_NAME);
        } else if (product.getProductName().length() > 255) {
            errors.add(Error.PRODUCT_NAME_TOO_LONG);
        }

        // Validate unit price
        if (product.getUnitPrice() == null) {
            errors.add(Error.PRODUCT_INVALID_PRICE);
        } else if (product.getUnitPrice() < 0) {
            errors.add(Error.PRODUCT_PRICE_TOO_LOW);
        }

        // Validate description
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            errors.add(Error.PRODUCT_INVALID_DESCRIPTION);
        }

        // Validate unit stock
        if (product.getUnitStock() == null) {
            errors.add(Error.PRODUCT_INVALID_STOCK);
        } else if (product.getUnitStock() < 0) {
            errors.add(Error.PRODUCT_STOCK_TOO_LOW);
        }

        // Throw exception if errors exist
        if (!errors.isEmpty()) {
            throw new CustomException(errors);
        }

        product.setId(getGenerationId());
        return productRepository.save(product);
    }

    @Override
    public Page<Product> getList(String search, Pageable pageable) {
        Page<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(search, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products;
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(()->new CustomException(Error.PRODUCT_NOT_FOUND));
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
