package com.r2s.mobile_store.presentation.mapper;

import com.r2s.mobile_store.application.dto.cart.CartDetailDTO;
import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartDetailMapper {
    @Autowired
    private ModelMapper modelMapper;
    public CartDetailDTO conventCartDetailToCart(CartDetail cartDetail){

        return CartDetailDTO.builder()
                .id(cartDetail.getId())
                .productName(cartDetail.getProduct().getProductName()
                ).quantity(cartDetail.getQuantity())
                .totalPrice(cartDetail.getTotalPrice())
                .unitPrice(cartDetail.getUnitPrice())
                .build();
    }
}
