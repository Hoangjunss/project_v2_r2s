package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Condition,Integer> {
}
