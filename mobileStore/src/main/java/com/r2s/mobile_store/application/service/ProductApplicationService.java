package com.r2s.mobile_store.application.service;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.domain.models.*;
import com.r2s.mobile_store.domain.service.*;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.presentation.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    @Transactional
    public ProductDto createProduct(ProductCreateDTO productCreateDTO) throws IOException {
        List<Error> errors = new ArrayList<>();


        MultipartFile file = productCreateDTO.getUrl();


            // Kiểm tra loại tệp, chỉ cho phép các loại ảnh nhất định
            String fileType = file.getContentType();
            List<String> allowedTypes = List.of("image/png", "image/jpeg", "image/jpg");
            if (fileType == null || !allowedTypes.contains(fileType)) {
                errors.add(Error.INVALID_FILE_TYPE);
            }

            // Kiểm tra kích thước tệp, giới hạn kích thước tối đa (ví dụ: 5MB)
            long maxFileSize = 10 * 1024 * 1024; // 5MB
            if (file.getSize() > maxFileSize) {
                errors.add(Error.FILE_SIZE_EXCEEDED);
            }


        // Throw exception if errors exist
        if (!errors.isEmpty()) {
            throw new CustomException(errors);
        }

        Category category=categoryService.findById(productCreateDTO.getCategory());

        Manufacturer manufacturer=manufacturerService.findById(productCreateDTO.getManufacturer());

        Condition condition=conditionService.findById(productCreateDTO.getCondition());

        log.info("product :{}",productCreateDTO.toString());

        ProductImage productImage=productImageService.addProductImage(productCreateDTO.getUrl());

        Product product=productMapper.conventProductCreateDTOToProduct(productCreateDTO,category,manufacturer,condition,productImage);

        return productMapper.conventProductToProductDto(productService.addProduct(product));
    }
    public Page<ProductDto> getList(String search,Pageable pageable) {
        return productService.getList(search,pageable).map(product -> productMapper.conventProductToProductDto(product));
    }
    public ProductDto findById(Integer id){
        return productMapper.conventProductToProductDto(productService.findById(id));
    }
}
