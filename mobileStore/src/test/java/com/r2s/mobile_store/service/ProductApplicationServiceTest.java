package com.r2s.mobile_store.service;



import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import com.r2s.mobile_store.domain.models.*;
import com.r2s.mobile_store.domain.service.*;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.presentation.mapper.ProductMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductApplicationServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private ConditionService conditionService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductImageService productImageService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductApplicationService productApplicationService;

    private ProductCreateDTO productCreateDTO;
    private Category category;
    private Manufacturer manufacturer;
    private Condition condition;
    private ProductImage productImage;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Sample Product");
        productCreateDTO.setUnitPrice(100.0);
        productCreateDTO.setDescription("Sample Description");
        productCreateDTO.setUnitStock(10);

        category = new Category();
        manufacturer = new Manufacturer();
        condition = new Condition();
        productImage = new ProductImage();
        product = new Product();
        productDto = new ProductDto();
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn(4 * 1024 * 1024L); // Kích thước dưới 5MB
        productCreateDTO.setUrl(file);

        when(categoryService.findById(any())).thenReturn(category);
        when(manufacturerService.findById(any())).thenReturn(manufacturer);
        when(conditionService.findById(any())).thenReturn(condition);
        when(productImageService.addProductImage(any())).thenReturn(productImage);
        when(productMapper.conventProductCreateDTOToProduct(any(), any(), any(), any(), any())).thenReturn(product);
        when(productService.addProduct(any())).thenReturn(product);
        when(productMapper.conventProductToProductDto(any())).thenReturn(productDto);

        ProductDto result = productApplicationService.createProduct(productCreateDTO);

        assertNotNull(result);
        assertEquals(productDto, result);

        verify(categoryService, times(1)).findById(any());
        verify(manufacturerService, times(1)).findById(any());
        verify(conditionService, times(1)).findById(any());
        verify(productImageService, times(1)).addProductImage(any());
        verify(productService, times(1)).addProduct(any());
    }

    @Test
    void createProduct_shouldThrowExceptionWhenProductNameIsInvalid() {
        productCreateDTO.setProductName(null);

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.createProduct(productCreateDTO));

        assertTrue(exception.getErrors().contains(Error.PRODUCT_INVALID_NAME));
    }

    @Test
    void createProduct_shouldThrowExceptionWhenPriceIsInvalid() {
        productCreateDTO.setUnitPrice(null);

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.createProduct(productCreateDTO));

        assertTrue(exception.getErrors().contains(Error.PRODUCT_INVALID_PRICE));
    }

    @Test
    void createProduct_shouldThrowExceptionWhenFileIsInvalid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        productCreateDTO.setUrl(file);

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.createProduct(productCreateDTO));

        assertTrue(exception.getErrors().contains(Error.FILE_NOT_FOUND));
    }

    @Test
    void createProduct_shouldThrowExceptionWhenFileTypeIsInvalid() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        productCreateDTO.setUrl(file);

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.createProduct(productCreateDTO));

        assertTrue(exception.getErrors().contains(Error.INVALID_FILE_TYPE));
    }

    @Test
    void createProduct_shouldThrowExceptionWhenFileSizeExceedsLimit() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn(11 * 1024 * 1024L);
        productCreateDTO.setUrl(file);

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.createProduct(productCreateDTO));

        assertTrue(exception.getErrors().contains(Error.FILE_SIZE_EXCEEDED));
    }

    @Test
    void getList_shouldReturnProductList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productService.getList(anyString(), any(Pageable.class))).thenReturn(page);
        when(productMapper.conventProductToProductDto(any(Product.class))).thenReturn(productDto);

        Page<ProductDto> result = productApplicationService.getList("Sample", pageable);

        assertEquals(1, result.getTotalElements());
        verify(productService, times(1)).getList(anyString(), any(Pageable.class));
    }

    @Test
    void findById_shouldReturnProductWhenFound() {
        when(productService.findById(anyInt())).thenReturn(product);
        when(productMapper.conventProductToProductDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productApplicationService.findById(1);

        assertNotNull(result);
        assertEquals(productDto, result);
    }

    @Test
    void findById_shouldThrowExceptionWhenProductNotFound() {
        when(productService.findById(anyInt())).thenThrow(new CustomException(singletonList(Error.PRODUCT_NOT_FOUND)));

        CustomException exception = assertThrows(CustomException.class, () -> productApplicationService.findById(1));

        assertEquals(singletonList(Error.PRODUCT_NOT_FOUND), exception.getErrors());
    }
}