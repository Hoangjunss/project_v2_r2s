package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
}
