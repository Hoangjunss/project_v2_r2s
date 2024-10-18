package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product addProduct(Product product) {
        if(product.getProductName() == null){
            throw new CustomException(Error.PRODUCT_INVALID_NAME);
        }
        if(product.getUnitPrice() == null){
            throw new CustomException(Error.PRODUCT_INVALID_PRICE);
        }
       if(product.getDescription()==null){
          throw new CustomException(Error.PRODUCT_INVALID_DESCRIPTION);
       }
        if(product.getUnitStock()==null){
          throw new CustomException(Error.PRODUCT_INVALID_STOCK);
        }

        product.setId(getGenerationId());
        return productRepository.save(product);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
