package com.r2s.mobile_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.presentation.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OurUserDetailsService ourUserDetailsService; // Thêm mock cho OurUserDetailsService

    @MockBean
    private JwtTokenUtil jwtTokenUtil; // Thêm mock cho JwtTokenUtil nếu cần

    @MockBean
    private ProductApplicationService productApplicationService;

    private ProductCreateDTO productCreateDTO;
    private ProductDto productDto;
    private Page<ProductDto> productPage;

    @BeforeEach
    void setUp() {
        productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Product");
        productCreateDTO.setUnitPrice(100.0);
        productCreateDTO.setUnitStock(10);
        productCreateDTO.setDescription("Test Description");
        productCreateDTO.setManufacturer(1);
        productCreateDTO.setCategory(1);
        productCreateDTO.setCondition(1);

        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setProductName("Test Product");
        productDto.setUnitPrice(100.0);
        productDto.setUnitStock(10);
        productDto.setDescription("Test Description");
        productDto.setManufacturer("Test Manufacturer");
        productDto.setCategory("Test Category");
        productDto.setCondition("New");
        productDto.setUrl("http://example.com/product-image.jpg");

        productPage = new PageImpl<>(List.of(productDto)); // Giả lập một trang chứa một sản phẩm
    }

    @Test
    void addProduct_shouldReturn201() throws Exception {
        when(productApplicationService.createProduct(any(ProductCreateDTO.class))).thenReturn(productDto);

        MockMultipartFile imageFile = new MockMultipartFile("url", "test-image.jpg", "image/jpeg", "image-content".getBytes());

        mockMvc.perform(multipart("/product")
                        .file(imageFile)
                        .param("productName", productCreateDTO.getProductName())
                        .param("unitPrice", productCreateDTO.getUnitPrice().toString())
                        .param("unitStock", productCreateDTO.getUnitStock().toString())
                        .param("description", productCreateDTO.getDescription())
                        .param("manufacturer", productCreateDTO.getManufacturer().toString())
                        .param("category", productCreateDTO.getCategory().toString())
                        .param("condition", productCreateDTO.getCondition().toString())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.id").value(productDto.getId()))
                .andExpect(jsonPath("$.data.productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.data.unitPrice").value(productDto.getUnitPrice()))
                .andExpect(jsonPath("$.data.unitStock").value(productDto.getUnitStock()))
                .andExpect(jsonPath("$.data.description").value(productDto.getDescription()))
                .andExpect(jsonPath("$.data.manufacturer").value(productDto.getManufacturer()))
                .andExpect(jsonPath("$.data.category").value(productDto.getCategory()))
                .andExpect(jsonPath("$.data.condition").value(productDto.getCondition()))
                .andExpect(jsonPath("$.data.url").value(productDto.getUrl()));

        verify(productApplicationService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        when(productApplicationService.getList(anyString(), any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/product")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search", "Test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Product list retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(productDto.getId()))
                .andExpect(jsonPath("$.data.content[0].productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.data.content[0].unitPrice").value(productDto.getUnitPrice()))
                .andExpect(jsonPath("$.data.content[0].unitStock").value(productDto.getUnitStock()))
                .andExpect(jsonPath("$.data.content[0].description").value(productDto.getDescription()))
                .andExpect(jsonPath("$.data.content[0].manufacturer").value(productDto.getManufacturer()))
                .andExpect(jsonPath("$.data.content[0].category").value(productDto.getCategory()))
                .andExpect(jsonPath("$.data.content[0].condition").value(productDto.getCondition()))
                .andExpect(jsonPath("$.data.content[0].url").value(productDto.getUrl()));

        verify(productApplicationService, times(1)).getList(anyString(), any(Pageable.class));
    }

    @Test
    void getId_shouldReturn200() throws Exception {
        when(productApplicationService.findById(anyInt())).thenReturn(productDto);

        mockMvc.perform(get("/product/id")
                        .param("id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Product retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(productDto.getId()))
                .andExpect(jsonPath("$.data.productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.data.unitPrice").value(productDto.getUnitPrice()))
                .andExpect(jsonPath("$.data.unitStock").value(productDto.getUnitStock()))
                .andExpect(jsonPath("$.data.description").value(productDto.getDescription()))
                .andExpect(jsonPath("$.data.manufacturer").value(productDto.getManufacturer()))
                .andExpect(jsonPath("$.data.category").value(productDto.getCategory()))
                .andExpect(jsonPath("$.data.condition").value(productDto.getCondition()))
                .andExpect(jsonPath("$.data.url").value(productDto.getUrl()));

        verify(productApplicationService, times(1)).findById(anyInt());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

