package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.*;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;
    private Product product;
    private Manufacturer manufacturer;
    private Category category;
    private Condition condition;
    private ProductImage productImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
        product.setUnitPrice(100.0);
        product.setUnitStock(10);
        product.setDescription("Test Description");

        // Tạo đối tượng Manufacturer
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1);
        manufacturer.setName("Test Manufacturer");

        // Tạo đối tượng Category
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        // Tạo đối tượng Condition
        Condition condition = new Condition();
        condition.setId(1);
        condition.setName("New");

        // Tạo đối tượng ProductImage
        ProductImage productImage = new ProductImage();
        productImage.setId(1);
        productImage.setUrl("test-url.jpg");

        // Set các quan hệ vào đối tượng Product
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        product.setCondition(condition);
        product.setProductImage(productImage);

    }
    @Test
    public void testAddProduct_Success() {
        // Giả lập hành vi của productRepository.save()
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Gọi phương thức cần test
        Product savedProduct = productService.addProduct(product);

        // Kiểm tra kết quả trả về
        assertNotNull(savedProduct);
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getManufacturer().getName(), savedProduct.getManufacturer().getName());
        assertEquals(product.getCategory().getName(), savedProduct.getCategory().getName());
        assertEquals(product.getCondition().getName(), savedProduct.getCondition().getName());
        assertEquals(product.getProductImage().getUrl(), savedProduct.getProductImage().getUrl());
        assertEquals(product.getUnitPrice(),savedProduct.getUnitPrice());
        assertEquals(product.getUnitStock(),savedProduct.getUnitStock());
        verify(productRepository, times(1)).save(product);
    }
    @Test
    public void testAddProduct_ProductNameIsNull() {
        // Thiết lập đối tượng Product có productName là null
        product.setProductName(null);

        // Kiểm tra xem CustomException có được ném ra không khi productName là null
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(product);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.PRODUCT_INVALID_NAME, exception.getError());
    }
    @Test
    public void testAddProduct_UnitPriceIsNull() {
        // Thiết lập đối tượng Product có unitPrice là null
        product.setUnitPrice(null);

        // Kiểm tra xem CustomException có được ném ra không khi unitPrice là null
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(product);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.PRODUCT_INVALID_PRICE, exception.getError());
    }
    @Test
    public void testAddProduct_DescriptionIsNull() {
        // Thiết lập đối tượng Product có description là null
        product.setDescription(null);

        // Kiểm tra xem CustomException có được ném ra không khi description là null
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(product);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.PRODUCT_INVALID_DESCRIPTION, exception.getError());
    }
    @Test
    public void testAddProduct_UnitStockIsNull() {
        // Thiết lập đối tượng Product có unitStock là null
        product.setUnitStock(null);

        // Kiểm tra xem CustomException có được ném ra không khi unitStock là null
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.addProduct(product);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.PRODUCT_INVALID_STOCK, exception.getError());
    }
    @Test
    public void testGetList_WithProducts() {
        // Tạo một danh sách sản phẩm với đối tượng Product mẫu
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        // Giả lập hành vi của productRepository.findAll()
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Gọi phương thức cần test
        Page<Product> result = productService.getList(pageable);

        // Kiểm tra kết quả trả về
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(product.getProductName(), result.getContent().get(0).getProductName());
        assertEquals(product.getManufacturer().getName(), result.getContent().get(0).getManufacturer().getName());
        assertEquals(product.getCategory().getName(), result.getContent().get(0).getCategory().getName());
        assertEquals(product.getCondition().getName(), result.getContent().get(0).getCondition().getName());
        assertEquals(product.getProductImage().getUrl(), result.getContent().get(0).getProductImage().getUrl());
        verify(productRepository, times(1)).findAll(pageable);
    }
    @Test
    public void testFindById_ProductExists() {
        // Giả lập hành vi của productRepository.findById()
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // Gọi phương thức cần test
        Product foundProduct = productService.findById(1);

        // Kiểm tra kết quả trả về
        assertEquals(product.getProductName(), foundProduct.getProductName());
        assertEquals(product.getManufacturer().getName(), foundProduct.getManufacturer().getName());
        assertEquals(product.getCategory().getName(), foundProduct.getCategory().getName());
        assertEquals(product.getCondition().getName(), foundProduct.getCondition().getName());
        assertEquals(product.getProductImage().getUrl(), foundProduct.getProductImage().getUrl());
        assertEquals(product.getUnitPrice(),foundProduct.getUnitPrice());
        assertEquals(product.getUnitStock(),foundProduct.getUnitStock());
        verify(productRepository, times(1)).findById(1);
    }
    @Test
    public void testFindById_ProductNotFound() {
        // Giả lập hành vi của productRepository.findById() để trả về Optional.empty()
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem CustomException có được ném ra không khi sản phẩm không tồn tại
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.findById(1);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.PRODUCT_NOT_FOUND, exception.getError());
        verify(productRepository, times(1)).findById(1);
    }
}
