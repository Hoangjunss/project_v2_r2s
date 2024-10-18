package com.r2s.mobile_store.presentation.mapper;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.domain.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    @Autowired
    private ModelMapper modelMapper;
    public Product conventProductCreateDTOToProduct(ProductCreateDTO productCreateDTO, Category category, Manufacturer manufacturer, Condition condition, ProductImage productImage){
      Product product=modelMapper.map(productCreateDTO,Product.class);

      product.setProductImage(productImage);
      product.setCategory(category);
      product.setManufacturer(manufacturer);
      product.setCondition(condition);

      return product;
    }
    public ProductDto conventProductToProductDto(Product product){
        ProductDto productDto=modelMapper.map(product, ProductDto.class);

        productDto.setCategory(product.getCategory().getName());
        productDto.setCondition(product.getCondition().getName());
        productDto.setManufacturer(product.getManufacturer().getName());
        productDto.setProductImage(product.getProductImage().getUrl());

        return productDto;
    }
}
