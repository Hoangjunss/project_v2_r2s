package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
}
