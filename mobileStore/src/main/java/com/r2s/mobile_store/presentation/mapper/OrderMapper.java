package com.r2s.mobile_store.presentation.mapper;


import com.r2s.mobile_store.application.dto.order.OrderCreateDTO;
import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.application.dto.order.OrderDetailDTO;

import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.repository.OrderDetailRepository;
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
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    public OrderDTO conventOrderToOrderDTO(Order order ,List<OrderDetail> orderDetailList){
        OrderDTO orderDTO=modelMapper.map(order, OrderDTO.class);

        List<OrderDetailDTO> orderDetails =orderDetailList
                .stream()
                .map(orderDetail -> orderDetailMapper.conventOrderDetailToOrder(orderDetail))
                .collect(Collectors.toList());
        orderDTO.setOrderDetails(orderDetails);
        return orderDTO;
    }
    public Order convertOrderDTOToOrder(OrderCreateDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        List<OrderDetail> orderDetails = orderDTO.getOrderDetailCreateDTOList()
                .stream()
                .map(orderDetailDTO -> orderDetailMapper.conventOrderDetailDTOToOrderDetail(orderDetailDTO))
                .collect(Collectors.toList());

        return order;
    }

}
