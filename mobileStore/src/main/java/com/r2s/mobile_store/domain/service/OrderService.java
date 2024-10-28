package com.r2s.mobile_store.domain.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.User;

public interface OrderService {
    Order findById(Integer id);
    Order addOrder(Integer idProduct,Integer quatity);
    Order deleteOrderDetail(Integer id);
    Order clearOrder();
    void createOrder(User user);
}
