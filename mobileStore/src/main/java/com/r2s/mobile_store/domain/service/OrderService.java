package com.r2s.mobile_store.domain.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.User;

import java.util.List;

public interface OrderService {

    Order addOrder(Order order, List<OrderDetail> orderDetails);

}
