package com.r2s.mobile_store.presentation.mapper;


import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;

import com.r2s.mobile_store.domain.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class OrderMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    public OrderDTO conventOrderToOrderDTO(Order order){
        OrderDTO orderDTO=modelMapper.map(order, OrderDTO.class);
        List<OrderDetailDTO> orderDetails =order.getOrderDetails()
                .stream()
                .map(orderDetail -> orderDetailMapper.conventOrderDetailToOrder(orderDetail))
                .collect(Collectors.toList());
        orderDTO.setOrderDetails(orderDetails);
        return orderDTO;
    }
}
