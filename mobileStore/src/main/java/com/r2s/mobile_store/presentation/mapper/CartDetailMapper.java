package com.r2s.mobile_store.presentation.mapper;

import com.r2s.mobile_store.application.dto.cart.CartDetailDTO;
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
                .productName(cartDetail.getProduct().getProductName())
                .quantity(cartDetail.getQuantity())
                .unitPrice(cartDetail.getProduct().getUnitPrice())
                .totalPrice(cartDetail.getProduct().getUnitPrice()*cartDetail.getQuantity())
                .build();
    }
}
