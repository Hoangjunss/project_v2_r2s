package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByUser(User user);
}
