package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface CartDetailService {
    CartDetail findById(Integer integer);
    CartDetail deleteCartDetail(Product product,Cart cart);
    void deleteAllCartDetail (Cart cart);
    CartDetail addCartDetail(Product product,Integer quantity,Cart cart);
    void updateCartDetailWhenAddProduct(Product product,Cart cart,Integer quantity);
    Optional<CartDetail> findByProductAndCart(Product product, Cart cart);
    List<CartDetail> findByCart(Integer cart);
    CartDetail updateCartDetail(Product product,Cart cart,Integer quantity);

}
