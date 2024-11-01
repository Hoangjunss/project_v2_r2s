package com.r2s.mobile_store.presentation.mapper;


import com.r2s.mobile_store.application.dto.order.OrderDetailCreateDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;

import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class OrderDetailMapper {
    @Autowired
    private ModelMapper modelMapper;

    public OrderDetailDTO conventOrderDetailToOrder(OrderDetail orderDetail){

        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .productName(orderDetail.getProduct().getProductName())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .unitPrice(orderDetail.getUnitPrice())
                .build();
    }
    public OrderDetail conventOrderDetailDTOToOrderDetail(OrderDetailCreateDTO orderDetailDTO){
        OrderDetail orderDetail=modelMapper.map(orderDetailDTO, OrderDetail.class);
        orderDetail.setId(null);
        log.info("id:{}",orderDetailDTO.getIdProduct());
        Product product = Product.builder().id(orderDetailDTO.getIdProduct()).build();


        orderDetail.setProduct(product);
        return orderDetail;
    }
}
