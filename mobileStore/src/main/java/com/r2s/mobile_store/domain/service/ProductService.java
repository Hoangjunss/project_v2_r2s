package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product addProduct(Product product);
    Page<Product> getList(String search,Pageable pageable);
    Product findById(Integer id);
}
