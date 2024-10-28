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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;

    @Override
    public Order findById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomException(Error.CART_NOT_FOUND));
    }

    @Override
    public Order addOrder( Integer idProduct, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Product product = productService.findById(idProduct);
        Order order=orderRepository.findByUser(currentUser);
        Optional<OrderDetail> optionalOrderDetail = orderDetailService.findByProductAndOrder(product, order);

        OrderDetail orderDetail;
        if (!optionalOrderDetail.isPresent()) {

            orderDetail=  orderDetailService.addOrderDetail(product,quantity,order);

            // Thêm CartDetail vào danh sách chi tiết của Cart
            order.getOrderDetails().add(orderDetail);
        } else {
            // Nếu đã có CartDetail, chỉ cập nhật số lượng
            orderDetail = optionalOrderDetail.get();

            orderDetailService.updateOrderDetail(orderDetail,quantity);
        }
        order.setQuantity(order.getQuantity() + quantity);  // Tăng số lượng tổng trong Cart
        order.setTotalPrice(order.getTotalPrice() + product.getUnitPrice() * quantity);

        return orderRepository.save(order);
    }

    @Override
    public Order deleteOrderDetail(Integer idOrderDetail) {
        OrderDetail orderDetail = orderDetailService.findById(idOrderDetail);

        // Lấy Cart mà CartDetail thuộc về
        Order order = orderDetail.getOrder();

        // Trừ số lượng và giá trị của CartDetail ra khỏi tổng trong Cart
        order.setQuantity(order.getQuantity() - orderDetail.getQuantity());
        order.setTotalPrice(order.getTotalPrice() - orderDetail.getTotalPrice());

        // Xóa CartDetail khỏi Cart
        order.getOrderDetails().remove(orderDetail);

        // Xóa CartDetail khỏi cơ sở dữ liệu
        orderDetailService.deleteOrderDetail(idOrderDetail);

        // Cập nhật lại Cart trong cơ sở dữ liệu
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order clearOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Order order=orderRepository.findByUser(currentUser);


        // Xóa tất cả CartDetail khỏi Cart
        List<OrderDetail> orderDetails = order.getOrderDetails();
        order.getOrderDetails().clear();
        orderDetailService.deleteAllOrderDetail(orderDetails);

        // Đặt lại số lượng và tổng giá trị trong Cart
        order.setQuantity(0);
        order.setTotalPrice(0.0);

        // Cập nhật lại Cart trong cơ sở dữ liệu
       return orderRepository.save(order);
    }

    @Override
    public void createOrder(User user) {
        Order order= Order.builder()
                .id(getGenerationId())
                .user(user)
                .totalPrice(Double.valueOf(0))
                .quantity(0)
                .build();
        orderRepository.save(order);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
