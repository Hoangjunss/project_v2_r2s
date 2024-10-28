package com.r2s.mobile_store.infrastructure.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;

import com.r2s.mobile_store.domain.repository.OrderDetailRepository;
import com.r2s.mobile_store.domain.service.OrderDetailService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail findById(Integer integer) {
        return orderDetailRepository.findById(integer)
                .orElseThrow(()-> new CustomException(Error.CARTDETAIL_NOT_FOUND)) ;
    }

    @Override
    public void deleteOrderDetail(Integer id) {
        OrderDetail orderDetail = findById(id);
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public void deleteAllOrderDetail(List<OrderDetail> orderDetails) {
        orderDetailRepository.deleteAll(orderDetails);
    }

    @Override
    public OrderDetail addOrderDetail(Product product, Integer quantity, Order order) {
        if(product.getUnitStock()<quantity){
            throw  new CustomException(Error.PRODUCT_UNABLE_TO_STOCK);
        }
        OrderDetail cartDetail=OrderDetail.builder()
                .id(getGenerationId())
                .order(order).product(product)
                .unitPrice(product.getUnitPrice())
                .quantity(quantity)
                .totalPrice(product.getUnitPrice()*quantity)
                .build();
       return orderDetailRepository.save(cartDetail);
    }

    @Override
    public void updateOrderDetail(OrderDetail orderDetail,Integer quantity) {
        orderDetail.setQuantity(orderDetail.getQuantity()+quantity);
        orderDetail.setTotalPrice(orderDetail.getUnitPrice()*orderDetail.getQuantity());
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public Optional<OrderDetail> findByProductAndOrder(Product product, Order order) {
        return orderDetailRepository.findByProductAndOrder(product,order);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
