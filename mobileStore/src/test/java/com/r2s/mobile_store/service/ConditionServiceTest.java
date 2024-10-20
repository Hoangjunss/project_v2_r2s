package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Condition;
import com.r2s.mobile_store.domain.repository.ConditionRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.ConditionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConditionServiceTest {
    @Mock
    private ConditionRepository conditionRepository;

    @InjectMocks
    private ConditionServiceImpl conditionService;

    private Condition condition;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Condition mẫu để test
        condition = new Condition();
        condition.setId(1);
        condition.setName("New Condition");
    }
    @Test
    public void testFindById_ConditionExists() {
        // Giả lập hành vi của conditionRepository.findById()
        when(conditionRepository.findById(1)).thenReturn(Optional.of(condition));

        // Gọi phương thức cần test
        Condition foundCondition = conditionService.findById(1);

        // Kiểm tra kết quả trả về
        assertNotNull(foundCondition);
        assertEquals(condition.getName(), foundCondition.getName());
        verify(conditionRepository, times(1)).findById(1);
    }
    @Test
    public void testFindById_ConditionNotFound() {
        // Giả lập hành vi của conditionRepository.findById() trả về Optional.empty()
        when(conditionRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem CustomException có được ném ra khi điều kiện không tồn tại
        CustomException exception = assertThrows(CustomException.class, () -> {
            conditionService.findById(1);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.CONDITION_NOT_FOUND, exception.getError());
        verify(conditionRepository, times(1)).findById(1);
    }
}
