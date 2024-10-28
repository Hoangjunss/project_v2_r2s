package com.r2s.mobile_store.application.service;

import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.domain.service.OrderService;

import com.r2s.mobile_store.presentation.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderApplicationService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    public OrderDTO addOrder(Integer idProduct, Integer quatity) {
        return orderMapper.conventOrderToOrderDTO(orderService.addOrder(idProduct, quatity));
    }

    public OrderDTO deleteOrderDetail(Integer idOrderDetail) {
        return orderMapper.conventOrderToOrderDTO(orderService.deleteOrderDetail(idOrderDetail));
    }

    public OrderDTO clearOrder() {
        return orderMapper.conventOrderToOrderDTO(orderService.clearOrder());
    }

    public OrderDTO findById(Integer id) {
        return orderMapper.conventOrderToOrderDTO(orderService.findById(id));
    }
}
