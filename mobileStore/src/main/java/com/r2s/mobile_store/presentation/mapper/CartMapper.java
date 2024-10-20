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
    public CartDTO conventCartToCartDTO(Cart cart){
        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        List<CartDetailDTO> cartDetails =cart.getCartDetails()
                .stream()
                .map(cartDetail -> cartDetailMapper.conventCartDetailToCart(cartDetail))
                .collect(Collectors.toList());
        cartDTO.setCartDetails(cartDetails);
        return cartDTO;
    }
}
