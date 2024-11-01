package com.r2s.mobile_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailCreateDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;
import com.r2s.mobile_store.application.service.OrderApplicationService;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.presentation.controller.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderApplicationService orderApplicationService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService; // Thêm mock cho OurUserDetailsService

    @MockBean
    private JwtTokenUtil jwtTokenUtil; // Thêm mock cho JwtTokenUtil nếu cần


    private OrderCreateDTO orderCreateDTO;
    private OrderDTO orderDTO;
    private OrderDetailDTO orderDetailDTO;
    private OrderDetailCreateDTO orderDetailCreateDTO;

    @BeforeEach
    void setUp() {
        // Tạo dữ liệu mẫu cho OrderDetailCreateDTO
        orderDetailCreateDTO = OrderDetailCreateDTO.builder()
                .idProduct(1)
                .quantity(2)
                .unitPrice(100.0)
                .totalPrice(200.0)
                .build();

        // Tạo dữ liệu mẫu cho OrderCreateDTO
        orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setOrderDetails(List.of(orderDetailCreateDTO));

        // Tạo dữ liệu mẫu cho OrderDetailDTO
        orderDetailDTO = OrderDetailDTO.builder()
                .id(1)
                .productName("Product Name")
                .quantity(2)
                .unitPrice(100.0)
                .totalPrice(200.0)
                .build();

        // Tạo dữ liệu mẫu cho OrderDTO
        orderDTO = OrderDTO.builder()
                .id(1)
                .userName("user123")
                .quantity(2)
                .totalPrice(200.0)
                .orderDetails(List.of(orderDetailDTO))
                .build();
    }

    @Test
    void addOrder_shouldReturn200() throws Exception {
        // Giả lập phản hồi từ orderApplicationService.addOrder
        when(orderApplicationService.addOrder(any(OrderCreateDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/order")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andExpect(jsonPath("$.data.id").value(orderDTO.getId()))
                .andExpect(jsonPath("$.data.userName").value(orderDTO.getUserName()))
                .andExpect(jsonPath("$.data.quantity").value(orderDTO.getQuantity()))
                .andExpect(jsonPath("$.data.totalPrice").value(orderDTO.getTotalPrice()))
                .andExpect(jsonPath("$.data.orderDetails[0].id").value(orderDetailDTO.getId()))
                .andExpect(jsonPath("$.data.orderDetails[0].productName").value(orderDetailDTO.getProductName()))
                .andExpect(jsonPath("$.data.orderDetails[0].quantity").value(orderDetailDTO.getQuantity()))
                .andExpect(jsonPath("$.data.orderDetails[0].unitPrice").value(orderDetailDTO.getUnitPrice()))
                .andExpect(jsonPath("$.data.orderDetails[0].totalPrice").value(orderDetailDTO.getTotalPrice()));

        verify(orderApplicationService, times(1)).addOrder(any(OrderCreateDTO.class));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
