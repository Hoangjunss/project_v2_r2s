package com.r2s.mobile_store.controller;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.application.dto.cart.CartDetailDTO;
import com.r2s.mobile_store.application.service.CartApplicationService;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.presentation.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

@WebMvcTest(ProductController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${server.servlet.context-path}")
    private String prefix;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private CartApplicationService cartApplicationService;
    private CartDTO cartDTO;
    private CartDetailDTO cartDetailDTO;
    private String idProduct;
    private String quantity;
    private String idCart;
    private String idCartDetail;

    @BeforeEach
    void setUp() {
        cartDTO=new CartDTO();
        cartDTO.setId(1);
        cartDTO.setQuantity(1);
        cartDTO.setUserName("cart");

        cartDetailDTO = new CartDetailDTO();
        cartDetailDTO.setId(1);
        cartDetailDTO.setProductName("Test Product");
        cartDetailDTO.setQuantity(1);
        cartDetailDTO.setUnitPrice(100.0);
        cartDetailDTO.setTotalPrice(100.0);
        cartDTO.setCartDetails(Collections.singletonList(cartDetailDTO));

        idProduct="1";

        quantity="1";

        idCart ="1";

        idCartDetail="1";

    }
    @Test
    public void testAddProductToCart() throws Exception {
        // Giả lập hành vi của cartApplicationService.addCart
        Mockito.when(cartApplicationService.addCart(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(cartDTO);

        mockMvc.perform(post(prefix+"/cart")
                        .param("idProduct", idProduct)
                        .param("quantity", quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cartDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(cartDTO.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(cartDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()))
                // So sánh thuộc tính của CartDetailDTO
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].id").value(cartDetailDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].productName").value(cartDetailDTO.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].quantity").value(cartDetailDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].unitPrice").value(cartDetailDTO.getUnitPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].totalPrice").value(cartDetailDTO.getTotalPrice()));
    }
    @Test
    public void testDeleteCartDetail() throws Exception {
        // Giả lập hành vi của cartApplicationService.deleteCartDetail
        Mockito.when(cartApplicationService.deleteCartDetail(Mockito.anyInt()))
                .thenReturn(cartDTO);

        mockMvc.perform(delete("/detail")
                        .param("idCartDetail", idCartDetail))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cartDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(cartDTO.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(cartDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()))
                // So sánh thuộc tính của CartDetailDTO
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].id").value(cartDetailDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].productName").value(cartDetailDTO.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].quantity").value(cartDetailDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].unitPrice").value(cartDetailDTO.getUnitPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].totalPrice").value(cartDetailDTO.getTotalPrice()));
    }
    @Test
    public void testClearCart() throws Exception {

        Mockito.when(cartApplicationService.clearCart()).thenReturn(cartDTO);

        mockMvc.perform(delete("/clear"))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cartDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(cartDTO.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(cartDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()))
                // So sánh thuộc tính của CartDetailDTO
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].id").value(cartDetailDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].productName").value(cartDetailDTO.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].quantity").value(cartDetailDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].unitPrice").value(cartDetailDTO.getUnitPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].totalPrice").value(cartDetailDTO.getTotalPrice()));
    }
    @Test
    public void testGetCartById() throws Exception {
        // Giả lập hành vi của cartApplicationService.findById
        Mockito.when(cartApplicationService.findById(Mockito.anyInt())).thenReturn(cartDTO);

        mockMvc.perform(get(prefix)
                        .param("idCart", idCart))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cartDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(cartDTO.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(cartDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()))
                // So sánh thuộc tính của CartDetailDTO
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].id").value(cartDetailDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].productName").value(cartDetailDTO.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].quantity").value(cartDetailDTO.getQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].unitPrice").value(cartDetailDTO.getUnitPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartDetails[0].totalPrice").value(cartDetailDTO.getTotalPrice()));
    }
}