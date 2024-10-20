package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Condition;
import com.r2s.mobile_store.domain.repository.ConditionRepository;
import com.r2s.mobile_store.domain.service.ConditionService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionServiceImpl implements ConditionService {
    @Autowired
    private ConditionRepository conditionRepository;
    @Override
    public Condition findById(Integer integer) {
        return conditionRepository.findById(integer).orElseThrow(()->new CustomException(Error.CONDITION_NOT_FOUND));
    }
}
