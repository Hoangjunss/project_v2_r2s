package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.OrderRepository;
import com.r2s.mobile_store.domain.service.OrderDetailService;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private User currentUser;
    private List<OrderDetail> orderDetails;

    @BeforeEach
    void setUp() {
        // Giả lập người dùng hiện tại
        currentUser = new User();
        currentUser.setId(UUID.randomUUID().variant());
        currentUser.setUsername("testuser");

        // Giả lập đối tượng Authentication và SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Tạo một đối tượng Order mẫu
        order = new Order();
        order.setTotalPrice(0.0);
        order.setQuantity(0);

        // Tạo danh sách OrderDetail mẫu
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setUnitPrice(500.0);
        orderDetail1.setQuantity(2);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setUnitPrice(300.0);
        orderDetail2.setQuantity(1);

        orderDetails = List.of(orderDetail1, orderDetail2);
    }

    @Test
    void addOrder_shouldSaveOrderWithCorrectTotalAmountAndQuantity() {
        // Giả lập hành vi lưu Order
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.addOrder(order, orderDetails);

        // Tính toán tổng tiền và tổng số lượng
        double expectedTotalAmount = orderDetails.stream().mapToDouble(od -> od.getUnitPrice() * od.getQuantity()).sum();
        int expectedTotalQuantity = orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum();

        // Xác minh kết quả
        assertEquals(currentUser, result.getUser());
        assertEquals(expectedTotalAmount, result.getTotalPrice());
        assertEquals(expectedTotalQuantity, result.getQuantity());

        // Xác minh rằng orderRepository.save được gọi
        verify(orderRepository, times(1)).save(any(Order.class));

        // Xác minh rằng mỗi OrderDetail được lưu vào DB với Order đã lưu
        orderDetails.forEach(orderDetail -> verify(orderDetailService, times(1)).addOrderDetail(orderDetail));
    }

    @Test
    void addOrder_shouldAssignOrderToEachOrderDetail() {
        // Giả lập hành vi lưu Order
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.addOrder(order, orderDetails);

        // Xác minh rằng mỗi OrderDetail đã được gán Order
        orderDetails.forEach(orderDetail -> assertEquals(result, orderDetail.getOrder()));

        // Xác minh rằng mỗi OrderDetail được lưu vào DB với Order đã lưu
        orderDetails.forEach(orderDetail -> verify(orderDetailService, times(1)).addOrderDetail(orderDetail));
    }
}

