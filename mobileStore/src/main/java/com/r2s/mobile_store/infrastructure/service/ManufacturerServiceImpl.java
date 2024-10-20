package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Manufacturer;
import com.r2s.mobile_store.domain.repository.ManufacturerRepository;
import com.r2s.mobile_store.domain.service.ManufacturerService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.r2s.mobile_store.infrastructure.exception.Error;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Override
    public Manufacturer findById(Integer integer) {
        return manufacturerRepository.findById(integer).orElseThrow(()->new CustomException(Error.MANUFACTURER_NOT_FOUND));
    }
}
