package com.r2s.mobile_store.infrastructure.service;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.OrderDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.models.User;

import com.r2s.mobile_store.domain.repository.OrderRepository;

import com.r2s.mobile_store.domain.service.OrderDetailService;
import com.r2s.mobile_store.domain.service.OrderService;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;



    @Override
    public Order addOrder(Order order, List<OrderDetail> orderDetails) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Gán người dùng cho Order và tạo ID cho Order
        order.setUser(currentUser);
        order.setId(getGenerationId());

        // Lưu Order trước khi gán vào OrderDetail
        orderRepository.save(order);

        // Sử dụng Stream API để xử lý OrderDetail
        orderDetails.forEach(orderDetail -> {
            orderDetail.setOrder(order); // Gán Order cho OrderDetail
            orderDetailService.addOrderDetail(orderDetail); // Lưu để unitPrice được cập nhật từ sản phẩm
            log.info("orderDetail after save: {}", orderDetail);
        });

        // Tính tổng tiền và tổng số lượng sau khi tất cả OrderDetail đã được lưu
        double totalAmount = orderDetails.stream()
                .mapToDouble(od -> od.getUnitPrice() * od.getQuantity())
                .sum();
        int totalQuantity = orderDetails.stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();


        order.setTotalPrice(totalAmount);
        order.setQuantity(totalQuantity);


        orderRepository.save(order);

        log.info("order: {}", order);
        log.info("orderDetails: {}", orderDetails);

        return order;
    }


    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
