package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.CartRepository;
import com.r2s.mobile_store.domain.service.CartDetailService;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartDetailService cartDetailService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartDetail cartDetail;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng User, Product, Cart, và CartDetail mẫu để test
        user = new User();
        user.setId(1);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setUnitPrice(100.0);
        product.setUnitStock(10);

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setQuantity(0);
        cart.setTotalPrice(0.0);

        cartDetail = new CartDetail();
        cartDetail.setId(1);
        cartDetail.setProduct(product);
        cartDetail.setCart(cart);
        cartDetail.setQuantity(1);
        cartDetail.setUnitPrice(100.0);
        cartDetail.setTotalPrice(100.0);

        cart.setCartDetails(Arrays.asList(cartDetail));
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        // Giả lập hành vi của getPrincipal() để trả về đối tượng User giả lập
        when(authentication.getPrincipal()).thenReturn(user);

        // Tạo một SecurityContext giả lập và thiết lập Authentication vào đó
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Đặt SecurityContext vào SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    public void testAddCart_AddNewProductToCart() {
        // Giả lập hành vi của productService và cartRepository
        when(productService.findById(1)).thenReturn(product);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(cartDetailService.findByProductAndCart(product, cart)).thenReturn(Optional.empty());
        when(cartDetailService.addCartDetail(any(Product.class), anyInt(), any(Cart.class))).thenReturn(cartDetail);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Gọi phương thức cần test
        Cart updatedCart = cartService.addCart(1, 2);

        // Kiểm tra kết quả trả về
        assertNotNull(updatedCart);
        assertEquals(2, updatedCart.getQuantity());
        assertEquals(200.0, updatedCart.getTotalPrice());
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    public void testAddCart_ProductNotFound() {
        // Giả lập hành vi của productService.findById() ném ngoại lệ
        when(productService.findById(1)).thenThrow(new CustomException(Error.PRODUCT_NOT_FOUND));

        // Kiểm tra xem CustomException có được ném ra khi không tìm thấy sản phẩm
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartService.addCart(1, 2);
        });

        // Kiểm tra mã lỗi của ngoại lệ
        assertEquals(Error.PRODUCT_NOT_FOUND, exception.getError());
        verify(productService, times(1)).findById(1);
    }

    @Test
    public void testFindById_CartNotFound() {
        // Giả lập hành vi của cartRepository.findById() trả về Optional.empty()
        when(cartRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem CustomException có được ném ra khi không tìm thấy giỏ hàng
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartService.findById(1);
        });

        // Kiểm tra mã lỗi của ngoại lệ
        assertEquals(Error.CART_NOT_FOUND, exception.getError());
        verify(cartRepository, times(1)).findById(1);
    }
    @Test
    public void testDeleteCartDetail_Success() {
        // Giả lập hành vi của cartDetailService.findById()
        when(cartDetailService.findById(1)).thenReturn(cartDetail);

        // Gọi phương thức cần test
        Cart updatedCart = cartService.deleteCartDetail(1);

        // Kiểm tra kết quả trả về
        assertNotNull(updatedCart);
        assertEquals(0, updatedCart.getQuantity());
        assertEquals(0.0, updatedCart.getTotalPrice());
        verify(cartDetailService, times(1)).deleteCartDetail(1);
        verify(cartRepository, times(1)).save(cart);
    }
    @Test
    public void testDeleteCartDetail_CartDetailNotFound() {
        // Giả lập hành vi của cartDetailService.findById() trả về Optional.empty()
        when(cartDetailService.findById(1)).thenThrow(new CustomException(Error.CARTDETAIL_NOT_FOUND));

        // Kiểm tra xem CustomException có được ném ra khi không tìm thấy chi tiết giỏ hàng
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartService.deleteCartDetail(1);
        });

        // Kiểm tra mã lỗi của ngoại lệ
        assertEquals(Error.CARTDETAIL_NOT_FOUND, exception.getError());
        verify(cartDetailService, times(1)).findById(1);
    }
    @Test
    public void testClearCart_CartNotFound() {
        // Giả lập hành vi của cartRepository.findByUser() trả về null
        when(cartRepository.findByUser(user)).thenReturn(null);

        // Kiểm tra xem CustomException có được ném ra khi không tìm thấy giỏ hàng
        CustomException exception = assertThrows(CustomException.class, () -> {
            cartService.clearCart();
        });

        // Kiểm tra mã lỗi của ngoại lệ
        assertEquals(Error.CART_NOT_FOUND, exception.getError());
        verify(cartRepository, times(1)).findByUser(user);
    }
    @Test
    public void testClearCart_Success() {
        // Tạo một đối tượng Cart hợp lệ
        Cart cart = new Cart();
        cart.setId(1);
        cart.setQuantity(5); // Giỏ hàng ban đầu có số lượng 5
        cart.setTotalPrice(500.0); // Tổng giá trị ban đầu là 500.0
        cart.setCartDetails(new ArrayList<>()); // Giỏ hàng có một danh sách CartDetail trống

        // Giả lập hành vi của cartRepository.findByUser() trả về đối tượng Cart hợp lệ
        when(cartRepository.findByUser(any(User.class))).thenReturn(cart);

        // Gọi phương thức cần test
        Cart clearedCart = cartService.clearCart();

        // Kiểm tra xem giỏ hàng có được cập nhật lại trong cơ sở dữ liệu hay không
        verify(cartRepository, times(1)).save(clearedCart);
    }

    @Test
    public void testCreateCart_Success() {
        // Gọi phương thức cần test
        cartService.createCart(user);

        // Kiểm tra xem phương thức save của repository có được gọi không
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
