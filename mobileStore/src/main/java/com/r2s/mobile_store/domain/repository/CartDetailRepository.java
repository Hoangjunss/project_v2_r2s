package com.r2s.mobile_store.domain.repository;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    Optional<CartDetail> findByProductAndCart(Product product, Cart cart);
    List<CartDetail>findAllByCart(Cart cart);
}
