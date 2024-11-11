package com.r2s.mobile_store.infrastructure.service;


import com.r2s.mobile_store.domain.models.*;

import com.r2s.mobile_store.domain.repository.OrderRepository;

import com.r2s.mobile_store.domain.service.*;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import jakarta.transaction.Transactional;
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
    @Autowired
    private CartService cartService;
    @Autowired
    private CartDetailService cartDetailService;


@Transactional
    @Override
    public Order addOrder() {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User)) {
            throw new CustomException(Error.USER_NOT_FOUND);
        }
        User currentUser = (User) authentication.getPrincipal();

        // Lấy giỏ hàng của người dùng
        Cart cart = cartService.findByUser();
        if (cart == null) {
            throw new CustomException(Error.CART_NOT_FOUND); // Xử lý khi giỏ hàng không tồn tại
        }

        // Tạo đối tượng Order và thiết lập thông tin cơ bản
        Order order = new Order();
        order.setUser(currentUser);
        order.setId(getGenerationId());
        order.setQuantity(cart.getQuantity());

        // Lấy danh sách CartDetail một lần duy nhất
        List<CartDetail> cartDetails = cartDetailService.findByCart(cart.getId());

        // Tính tổng giá từ danh sách CartDetail
        double totalPrice = cartDetails.stream()
                .mapToDouble(cartDetail -> cartDetail.getProduct().getUnitPrice() * cartDetail.getQuantity())
                .sum();

        // Gán totalPrice cho Order và lưu Order trước
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Tạo và lưu từng OrderDetail
        cartDetails.stream()
                .map(cartDetail -> OrderDetail.builder()
                        .order(order)
                        .product(cartDetail.getProduct())
                        .quantity(cartDetail.getQuantity())
                        .unitPrice(cartDetail.getProduct().getUnitPrice())
                        .totalPrice(cartDetail.getProduct().getUnitPrice() * cartDetail.getQuantity())
                        .build())
                .forEach(orderDetailService::addOrderDetail);
        cartService.clearCart();

        return order;
    }

    @Override
    public List<Order> getOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Integer id) {
        return orderRepository.findById(id).orElseThrow(()->new CustomException(Error.ORDER_NOT_FOUND));
    }


    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
