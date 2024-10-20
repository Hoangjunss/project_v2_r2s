package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Category;
import com.r2s.mobile_store.domain.repository.CategoryRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Category mẫu để test
        category = new Category();
        category.setId(1);
        category.setName("Test Category");
    }
    @Test
    public void testFindById_CategoryExists() {
        // Giả lập hành vi của categoryRepository.findById()
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Gọi phương thức cần test
        Category foundCategory = categoryService.findById(1);

        // Kiểm tra kết quả trả về
        assertNotNull(foundCategory);
        assertEquals(category.getName(), foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1);
    }
    @Test
    public void testFindById_CategoryNotFound() {
        // Giả lập hành vi của categoryRepository.findById() trả về Optional.empty()
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem CustomException có được ném ra khi danh mục không tồn tại
        CustomException exception = assertThrows(CustomException.class, () -> {
            categoryService.findById(1);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.CATEGORY_NOT_FOUND, exception.getError());
        verify(categoryRepository, times(1)).findById(1);
    }

}
