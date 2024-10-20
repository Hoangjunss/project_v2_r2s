package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Manufacturer;
import com.r2s.mobile_store.domain.repository.ManufacturerRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.service.ManufacturerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManufacturerServiceTest {
    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    private Manufacturer manufacturer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Manufacturer mẫu để test
        manufacturer = new Manufacturer();
        manufacturer.setId(1);
        manufacturer.setName("Test Manufacturer");
    }
    @Test
    public void testFindById_ManufacturerExists() {
        // Giả lập hành vi của manufacturerRepository.findById()
        when(manufacturerRepository.findById(1)).thenReturn(Optional.of(manufacturer));

        // Gọi phương thức cần test
        Manufacturer foundManufacturer = manufacturerService.findById(1);

        // Kiểm tra kết quả trả về
        assertNotNull(foundManufacturer);
        assertEquals(manufacturer.getName(), foundManufacturer.getName());
        verify(manufacturerRepository, times(1)).findById(1);
    }
    @Test
    public void testFindById_ManufacturerNotFound() {
        // Giả lập hành vi của manufacturerRepository.findById() trả về Optional.empty()
        when(manufacturerRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem CustomException có được ném ra khi nhà sản xuất không tồn tại
        CustomException exception = assertThrows(CustomException.class, () -> {
            manufacturerService.findById(1);
        });

        // Kiểm tra xem mã lỗi có đúng không
        assertEquals(Error.MANUFACTURER_NOT_FOUND, exception.getError());
        verify(manufacturerRepository, times(1)).findById(1);
    }
}
