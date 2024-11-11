package com.r2s.mobile_store.application.service;


import com.r2s.mobile_store.application.dto.order.OrderCreateDTO;
import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;
import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.service.OrderDetailService;
import com.r2s.mobile_store.domain.service.OrderService;

import com.r2s.mobile_store.presentation.mapper.OrderDetailMapper;
import com.r2s.mobile_store.presentation.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderApplicationService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailService orderDetailService;

    public OrderDTO addOrder() {
        Order order=orderService.addOrder();
        List<OrderDetail> orderDetails=orderDetailService.getOrderDetailByOrder(order);
        return orderMapper.conventOrderToOrderDTO(order,orderDetails);
    }
    public List<OrderDTO> getAll(){
        return orderService.getOrder().stream().map(order -> {
            List<OrderDetail> orderDetails=orderDetailService.getOrderDetailByOrder(order);
            return orderMapper.conventOrderToOrderDTO(order,orderDetails);
        }).collect(Collectors.toList());
    }
    public OrderDTO findById(Integer id){
        Order order=orderService.findById(id);
        List<OrderDetail> orderDetails=orderDetailService.getOrderDetailByOrder(order);
        return orderMapper.conventOrderToOrderDTO(order,orderDetails);
    }


}
