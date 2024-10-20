package com.r2s.mobile_store.application.service;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.domain.service.CartService;
import com.r2s.mobile_store.presentation.mapper.CartMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartApplicationService {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartMapper cartMapper;
    public CartDTO addCart(Integer idProduct,Integer quatity){
        return cartMapper.conventCartToCartDTO(cartService.addCart(idProduct, quatity));
    }

    public CartDTO deleteCartDetail(Integer idCartDetail){
        return cartMapper.conventCartToCartDTO(cartService.deleteCartDetail(idCartDetail));
    }

    public CartDTO clearCart(){
        return cartMapper.conventCartToCartDTO(cartService.clearCart());
    }

    public CartDTO findById(Integer id){
        return cartMapper.conventCartToCartDTO(cartService.findById(id));
    }
}
