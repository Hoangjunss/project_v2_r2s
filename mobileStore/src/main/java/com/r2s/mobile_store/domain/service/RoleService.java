package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    Role findById(Integer id);
    Role findByName(String name);
}
