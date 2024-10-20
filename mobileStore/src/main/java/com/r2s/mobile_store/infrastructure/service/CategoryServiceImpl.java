package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Category;
import com.r2s.mobile_store.domain.repository.CategoryRepository;
import com.r2s.mobile_store.domain.service.CategoryService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category findById(Integer integer) {
        return categoryRepository.findById(integer).orElseThrow(()->new CustomException(Error.CATEGORY_NOT_FOUND));
    }
}
