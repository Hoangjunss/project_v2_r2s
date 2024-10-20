package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.User;

public interface CartService {
    Cart findById(Integer id);
    Cart addCart(Integer idProduct,Integer quatity);
    Cart deleteCartDetail(Integer id);
    Cart clearCart();
    Cart createCart(User user);
}
