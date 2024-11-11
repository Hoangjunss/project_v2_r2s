package com.r2s.mobile_store.application.service;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.service.CartDetailService;
import com.r2s.mobile_store.domain.service.CartService;
import com.r2s.mobile_store.presentation.mapper.CartMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartApplicationService {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private CartMapper cartMapper;
    public CartDTO addCart(Integer idProduct,Integer quatity){
        Cart cart=cartService.addCart(idProduct, quatity);

        List<CartDetail> cartDetails=cartDetailService.findByCart(cart.getId());

        return cartMapper.conventCartToCartDTO(cart,cartDetails);
    }

    public CartDTO deleteCartDetail(Integer idCartDetail){
        Cart cart=cartService.deleteCartDetail(idCartDetail);

        List<CartDetail> cartDetails=cartDetailService.findByCart(cart.getId());

        return cartMapper.conventCartToCartDTO(cart,cartDetails);
    }

    public void clearCart(){
         cartService.clearCart();
    }

    public CartDTO findCart(){
       Cart cart=cartService.findByUser();
        if (cart == null) {
            return null;
        }

        List<CartDetail> cartDetails=cartDetailService.findByCart(cart.getId());

        return cartMapper.conventCartToCartDTO(cart,cartDetails);
    }
    public CartDTO updateCart(Integer idProduct,Integer quantity){
        Cart cart=cartService.updateCart(idProduct, quantity);

        List<CartDetail> cartDetails=cartDetailService.findByCart((cart.getId()));

        return cartMapper.conventCartToCartDTO(cart,cartDetails);
    }
}
