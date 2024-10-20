package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface CartDetailService {
    CartDetail findById(Integer integer);
    void deleteCartDetail(Integer id);
    void deleteAllCartDetail (List<CartDetail> cartDetails);
    CartDetail addCartDetail(Product product,Integer quantity,Cart cart);
    void updateCartDetail(CartDetail cartDetail,Integer quantity);
    Optional<CartDetail> findByProductAndCart(Product product, Cart cart);

}
