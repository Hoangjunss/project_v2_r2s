package com.r2s.mobile_store.presentation.mapper;


import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;

import com.r2s.mobile_store.domain.models.OrderDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {
    @Autowired
    private ModelMapper modelMapper;
    public OrderDetailDTO conventOrderDetailToOrder(OrderDetail orderDetail){

        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .productName(orderDetail.getProduct().getProductName()
                ).quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .unitPrice(orderDetail.getUnitPrice())
                .build();
    }
}
