package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.CartDetailRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.CartDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartDetailServiceTest {
    @Mock
    private CartDetailRepository cartDetailRepository;

    @InjectMocks
    private CartDetailServiceImpl cartDetailService;

    private CartDetail cartDetail;
    private Product product;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Product và Cart mẫu để test
        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setUnitPrice(100.0);
        product.setUnitStock(10);

        cart = new Cart();
        cart.setId(1);

        // Tạo đối tượng CartDetail mẫu để test
        cartDetail = new CartDetail();
        cartDetail.setId(1);
        cartDetail.setProduct(product);
        cartDetail.setCart(cart);
        cartDetail.setQuantity(1);
        cartDetail.setUnitPrice(100.0);
        cartDetail.setTotalPrice(100.0);
    }
    @Test
    public void testFindById_CartDetailExists() {
        // Giả lập hành vi của cartDetailRepository.findById()
        when(cartDetailRepository.findById(1)).thenReturn(Optional.of(cartDetail));

        // Gọi phương thức cần test
        CartDetail foundCartDetail = cartDetailService.findById(1);

        // Kiểm tra kết quả trả về
        assertNotNull(foundCartDetail);
        assertEquals(cartDetail.getId(), foundCartDetail.getId());
        assertEquals(cartDetail.getProduct().getId(), foundCartDetail.getProduct().getId());
        assertEquals(cartDetail.getCart().getId(), foundCartDetail.getCart().getId());
        assertEquals(cartDetail.getQuantity(), foundCartDetail.getQuantity());
        assertEquals(cartDetail.getUnitPrice(), foundCartDetail.getUnitPrice());
        assertEquals(cartDetail.getTotalPrice(), foundCartDetail.getTotalPrice());
        verify(cartDetailRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_CartDetailNotFound() {
        // Giả lập hành vi của cartDetailRepository.findById() trả về Optional.empty()
        when(cartDetailRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem ngoại lệ có được ném ra khi không tìm thấy chi tiết giỏ hàng
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartDetailService.findById(1);
        });

        assertEquals(Error.CARTDETAIL_NOT_FOUND, exception.getError());
        verify(cartDetailRepository, times(1)).findById(1);
    }
    @Test
    public void testUpdateCartDetail_Success() {
        // Giả lập hành vi của cartDetailRepository.save()
        when(cartDetailRepository.save(any(CartDetail.class))).thenReturn(cartDetail);

        // Gọi phương thức cần test
        cartDetailService.updateCartDetail(cartDetail, 2);

        // Kiểm tra kết quả trả về
        assertEquals(3, cartDetail.getQuantity());
        assertEquals(300.0, cartDetail.getTotalPrice());
        verify(cartDetailRepository, times(1)).save(cartDetail);
    }
    @Test
    public void testDeleteCartDetail_Success() {
        // Giả lập hành vi của cartDetailRepository.findById()
        when(cartDetailRepository.findById(1)).thenReturn(Optional.of(cartDetail));

        // Gọi phương thức cần test
        cartDetailService.deleteCartDetail(1);

        // Kiểm tra xem phương thức delete có được gọi không
        verify(cartDetailRepository, times(1)).delete(cartDetail);
    }
    @Test
    public void testDeleteAllCartDetail_Success() {
        // Gọi phương thức cần test
        cartDetailService.deleteAllCartDetail(Arrays.asList(cartDetail));

        // Kiểm tra xem phương thức deleteAll có được gọi không
        verify(cartDetailRepository, times(1)).deleteAll(anyList());
    }
}
