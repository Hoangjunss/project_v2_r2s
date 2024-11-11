package com.r2s.mobile_store.service;


import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setProductName("Sample Product");
        product.setUnitPrice(15000.0);
        product.setDescription("This is a sample product.");
        product.setUnitStock(10);
    }

    @Test
    void addProduct_shouldSaveProductWhenValid() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals(product.getProductName(), result.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_shouldThrowExceptionWhenProductNameIsInvalid() {
        product.setProductName(null);

        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));

        assertEquals(singletonList(Error.PRODUCT_INVALID_NAME), exception.getErrors());
    }

    @Test
    void getList_shouldReturnAllProductsWhenNoSearchTerm() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.getList(null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_shouldReturnProductWhenFound() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product result = productService.findById(product.getId());

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    void findById_shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.findById(product.getId()));

        assertEquals(singletonList(Error.PRODUCT_NOT_FOUND), exception.getErrors());
    }
}
