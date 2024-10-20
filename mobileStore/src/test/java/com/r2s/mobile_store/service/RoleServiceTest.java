package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.Role;
import com.r2s.mobile_store.domain.repository.RoleRepository;
import com.r2s.mobile_store.infrastructure.service.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo đối tượng Role mẫu để test
        role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");
    }
    @Test
    public void testFindById_RoleExists() {
        // Giả lập hành vi của roleRepository.findById()
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        // Gọi phương thức cần test
        Role foundRole = roleService.findById(1);

        // Kiểm tra kết quả trả về
        assertNotNull(foundRole);
        assertEquals(role.getName(), foundRole.getName());
        verify(roleRepository, times(1)).findById(1);
    }
    @Test
    public void testFindById_RoleNotFound() {
        // Giả lập hành vi của roleRepository.findById() trả về Optional.empty()
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        // Kiểm tra xem ngoại lệ có được ném ra khi vai trò không tồn tại
        assertThrows(RuntimeException.class, () -> {
            roleService.findById(1);
        });

        verify(roleRepository, times(1)).findById(1);
    }
    @Test
    public void testFindByName_RoleExists() {
        // Giả lập hành vi của roleRepository.findByName()
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        // Gọi phương thức cần test
        Role foundRole = roleService.findByName("ROLE_ADMIN");

        // Kiểm tra kết quả trả về
        assertNotNull(foundRole);
        assertEquals(role.getName(), foundRole.getName());
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
    }
    @Test
    public void testFindByName_RoleNotFound() {
        // Giả lập hành vi của roleRepository.findByName() trả về Optional.empty()
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        // Kiểm tra xem ngoại lệ có được ném ra khi vai trò không tồn tại
        assertThrows(RuntimeException.class, () -> {
            roleService.findByName("ROLE_ADMIN");
        });

        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
    }
}
