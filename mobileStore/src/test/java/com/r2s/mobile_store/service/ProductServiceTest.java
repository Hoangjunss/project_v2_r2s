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
import java.util.UUID;

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
        product.setId(UUID.randomUUID().variant());
        product.setProductName("Sample Product");
        product.setUnitPrice(15000.0);
        product.setDescription("This is a sample product.");
        product.setUnitStock(10);
    }

    @Test
    void addProduct_shouldSaveProductWhenValid() {
        // Giả lập khi lưu sản phẩm thành công
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals(product.getProductName(), result.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_shouldThrowExceptionWhenProductNameIsNull() {
        product.setProductName(null);
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_INVALID_NAME, exception.getError());
    }

    @Test
    void addProduct_shouldThrowExceptionWhenProductNameIsEmpty() {
        product.setProductName("");
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_INVALID_NAME, exception.getError());
    }

    @Test
    void addProduct_shouldThrowExceptionWhenProductNameTooLong() {
        product.setProductName("A".repeat(256));
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_NAME_TOO_LONG, exception.getError());
    }

    @Test
    void addProduct_shouldThrowExceptionWhenPriceIsTooLow() {
        product.setUnitPrice(5000.0);
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_PRICE_TOO_LOW, exception.getError());
    }


    @Test
    void addProduct_shouldThrowExceptionWhenDescriptionIsNull() {
        product.setDescription(null);
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_INVALID_DESCRIPTION, exception.getError());
    }

    @Test
    void addProduct_shouldThrowExceptionWhenUnitStockIsTooLow() {
        product.setUnitStock(-1);
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(product));
        assertEquals(Error.PRODUCT_STOCK_TOO_LOW, exception.getError());
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
    void getList_shouldReturnFilteredProductsWhenSearchTermProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findByProductNameContainingIgnoreCase("Sample", pageable)).thenReturn(page);

        Page<Product> result = productService.getList("Sample", pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findByProductNameContainingIgnoreCase("Sample", pageable);
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
        assertEquals(Error.PRODUCT_NOT_FOUND, exception.getError());
    }
}
