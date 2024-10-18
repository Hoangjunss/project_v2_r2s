package com.r2s.mobile_store.application.service;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.domain.models.*;
import com.r2s.mobile_store.domain.service.*;
import com.r2s.mobile_store.presentation.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductApplicationService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductMapper productMapper;
    public ProductDto createProduct(ProductCreateDTO productCreateDTO) throws IOException {
        Category category=categoryService.findById(productCreateDTO.getCategory());
        Manufacturer manufacturer=manufacturerService.findById(productCreateDTO.getManufacturer());
        Condition condition=conditionService.findById(productCreateDTO.getCondition());
        ProductImage productImage=productImageService.addProductImage(productCreateDTO.getProductImage());

        Product product=productMapper.conventProductCreateDTOToProduct(productCreateDTO,category,manufacturer,condition,productImage);

        return productMapper.conventProductToProductDto(productService.addProduct(product));
    }
}
