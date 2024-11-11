package com.r2s.mobile_store.presentation.mapper;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.application.dto.cart.CartDetailDTO;
import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.stream.Collectors;
@Component
public class CartMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartDetailMapper cartDetailMapper;
    public CartDTO conventCartToCartDTO(Cart cart, List <CartDetail> cartDetailList){
        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        final double[] totalPrice = {0.0};
        final int[] quantity = {0};

        List<CartDetailDTO> cartDetails =cartDetailList
                .stream()
                .map(cartDetail -> {
                    CartDetailDTO cartDetailDTO=cartDetailMapper.conventCartDetailToCart(cartDetail);
                    quantity[0] += cartDetailDTO.getQuantity();
                    totalPrice[0] += cartDetailDTO.getTotalPrice();
                    return  cartDetailDTO;

                })
                .collect(Collectors.toList());
        cartDTO.setCartDetails(cartDetails);
        cartDTO.setQuantity(quantity[0]);
        cartDTO.setTotalPrice(totalPrice[0]);
        return cartDTO;
    }
}
