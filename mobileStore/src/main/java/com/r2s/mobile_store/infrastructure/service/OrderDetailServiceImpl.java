package com.r2s.mobile_store.infrastructure.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;

import com.r2s.mobile_store.domain.repository.OrderDetailRepository;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.domain.service.OrderDetailService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public OrderDetail addOrderDetail(OrderDetail orderDetail) {
        List<Error> errors = new ArrayList<>();
        Product product = productRepository.findById(orderDetail.getProduct().getId())
                .orElseThrow(() -> new CustomException(List.of(Error.PRODUCT_NOT_FOUND), String.valueOf(orderDetail.getProduct().getId())));

        if (product.getUnitStock() < orderDetail.getQuantity()) {
            errors.add(Error.PRODUCT_UNABLE_TO_STOCK);
        }
        if (orderDetail.getQuantity() <= 0) {
            errors.add(Error.QUANTITY_LOW);
        }

        if (!errors.isEmpty()) {
            throw new CustomException(errors, String.valueOf(product.getId())); // Pass the product ID here
        }

        product.setUnitStock(product.getUnitStock() - orderDetail.getQuantity());
        productRepository.save(product);

        orderDetail.setId(getGenerationId());
        orderDetail.setUnitPrice(product.getUnitPrice());
        orderDetail.setTotalPrice(product.getUnitPrice() * orderDetail.getQuantity());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrder(Order order) {
        return orderDetailRepository.findAllByOrder(order);
    }


    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
