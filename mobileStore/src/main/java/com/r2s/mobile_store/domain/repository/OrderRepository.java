package com.r2s.mobile_store.domain.repository;


import com.r2s.mobile_store.domain.models.Order;
import com.r2s.mobile_store.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByUser(User user);
}
