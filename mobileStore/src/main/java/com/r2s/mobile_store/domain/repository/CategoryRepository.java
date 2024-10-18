package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
