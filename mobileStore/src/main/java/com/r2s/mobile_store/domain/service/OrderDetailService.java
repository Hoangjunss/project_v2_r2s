package com.r2s.mobile_store.domain.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

    OrderDetail addOrderDetail(OrderDetail orderDetail);

}
