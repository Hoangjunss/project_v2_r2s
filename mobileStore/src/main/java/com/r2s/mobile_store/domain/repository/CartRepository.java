package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUser(User user);
}
