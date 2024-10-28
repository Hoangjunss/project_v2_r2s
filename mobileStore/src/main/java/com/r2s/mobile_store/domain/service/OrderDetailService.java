package com.r2s.mobile_store.domain.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    OrderDetail findById(Integer integer);
    void deleteOrderDetail(Integer id);
    void deleteAllOrderDetail (List<OrderDetail> orderDetails);
    OrderDetail addOrderDetail(Product product, Integer quantity, Order order);
    void updateOrderDetail(OrderDetail orderDetail,Integer quantity);
    Optional<OrderDetail> findByProductAndOrder(Product product, Order order);

}
