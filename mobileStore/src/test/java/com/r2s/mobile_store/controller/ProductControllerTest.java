package com.r2s.mobile_store.controller;


import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.presentation.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${server.servlet.context-path}")
    private String prefix;

    @MockBean
    private ProductApplicationService productService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private ProductCreateDTO createProductRequest;

    private ProductDto productDTO;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("file", "product.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        createProductRequest = new ProductCreateDTO();

        createProductRequest.setProductName("Product 1");

        createProductRequest.setUnitPrice(100.0);
        createProductRequest.setUnitStock(100);

        createProductRequest.setCategory(1);

        createProductRequest.setUrl(mockFile);

        createProductRequest.setCondition(1);

        createProductRequest.setManufacturer(1);

        createProductRequest.setDescription("description");



        productDTO = new ProductDto();

        productDTO.setId(1);

        productDTO.setProductName("Updated Product");

        productDTO.setUnitPrice(100.0);

        productDTO.setUrl("/image");

        productDTO.setManufacturer("");

        productDTO.setCondition("");

        productDTO.setDescription("");

        productDTO.setCategory("brand");


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_shouldReturn201() throws Exception {


        when(productService.createProduct(any(ProductCreateDTO.class)))
                .thenReturn(productDTO);

        mockMvc.perform(multipart(prefix+"/product") // Gửi file multipart
                        .file(mockFile)
                        .with(csrf())
                        .param("productName", createProductRequest.getProductName())
                        .param("unitPrice", String.valueOf(createProductRequest.getUnitPrice()))
                        .param("unitStock", String.valueOf(createProductRequest.getUnitStock()))
                        .param("description", createProductRequest.getDescription())
                        .param("manufacturer", String.valueOf(createProductRequest.getManufacturer()))
                        .param("category", String.valueOf(createProductRequest.getCategory()))
                        .param("condition", String.valueOf(createProductRequest.getCondition())))


                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.productName").value(productDTO.getProductName()))
                .andExpect(jsonPath("$.unitPrice").value(productDTO.getUnitPrice()))
                .andExpect(jsonPath("$.unitStock").value(productDTO.getUnitStock()))
                .andExpect(jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(jsonPath("$.manufacturer").value(productDTO.getManufacturer()))
                .andExpect(jsonPath("$.category").value(productDTO.getCategory()))
                .andExpect(jsonPath("$.url").value(productDTO.getUrl()));

        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }


    @Test
    @WithMockUser(roles = "USER")
    void getAllProducts_shouldReturn200() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        when(productService.getList(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(productDTO)));

        mockMvc.perform(get(prefix+"/product?page=0&size=10").with(csrf()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.productName").value(productDTO.getProductName()))
                .andExpect(jsonPath("$.unitPrice").value(productDTO.getUnitPrice()))
                .andExpect(jsonPath("$.unitStock").value(productDTO.getUnitStock()))
                .andExpect(jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(jsonPath("$.manufacturer").value(productDTO.getManufacturer()))
                .andExpect(jsonPath("$.category").value(productDTO.getCategory()))
                .andExpect(jsonPath("$.url").value(productDTO.getUrl()));

        verify(productService, times(1)).getList(pageable);
    }
}
