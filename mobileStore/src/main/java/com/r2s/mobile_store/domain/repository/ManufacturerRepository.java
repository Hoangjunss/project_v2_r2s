package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,Integer> {
}
