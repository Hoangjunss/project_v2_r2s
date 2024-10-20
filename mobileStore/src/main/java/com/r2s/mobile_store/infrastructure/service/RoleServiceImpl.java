package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Role;
import com.r2s.mobile_store.domain.repository.RoleRepository;
import com.r2s.mobile_store.domain.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).orElseThrow();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }


}
