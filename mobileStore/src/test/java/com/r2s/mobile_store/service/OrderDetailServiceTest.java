package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.OrderDetailRepository;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.service.OrderDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderDetailServiceImpl orderDetailService;

    private Product product;
    private OrderDetail orderDetail;

    @BeforeEach
    void setUp() {
        // Set up a sample Product
        product = new Product();
        product.setId(UUID.randomUUID().variant());
        product.setProductName("Sample Product");
        product.setUnitStock(10);
        product.setUnitPrice(100.0);

        // Set up a sample OrderDetail
        orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(5);
    }

    @Test
    void addOrderDetail_shouldSaveOrderDetailWhenProductExistsAndInStock() {
        // Mock product exists in DB and has sufficient stock
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(orderDetail);

        OrderDetail result = orderDetailService.addOrderDetail(orderDetail);

        // Calculate expected total price and remaining stock
        double expectedTotalPrice = product.getUnitPrice() * orderDetail.getQuantity();
        int expectedStock = product.getUnitStock() - orderDetail.getQuantity();

        // Verify results
        assertNotNull(result);
        assertEquals(expectedTotalPrice, result.getTotalPrice());
        assertEquals(product.getUnitPrice(), result.getUnitPrice());

        verify(productRepository, times(1)).save(product);
        verify(orderDetailRepository, times(1)).save(orderDetail);
    }

    @Test
    void addOrderDetail_shouldThrowExceptionWhenProductNotFound() {
        // Mock product not found in DB
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderDetailService.addOrderDetail(orderDetail));

        assertEquals(singletonList(Error.PRODUCT_NOT_FOUND), exception.getErrors());
        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
    }

    @Test
    void addOrderDetail_shouldThrowExceptionWhenStockInsufficient() {
        // Mock insufficient stock
        product.setUnitStock(3);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        CustomException exception = assertThrows(CustomException.class, () -> orderDetailService.addOrderDetail(orderDetail));

        assertEquals(singletonList(Error.PRODUCT_UNABLE_TO_STOCK), exception.getErrors());
        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
    }
}
