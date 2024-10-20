package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.CartDetailRepository;
import com.r2s.mobile_store.domain.service.CartDetailService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Override
    public CartDetail findById(Integer integer) {
        return cartDetailRepository.findById(integer)
                .orElseThrow(()-> new CustomException(Error.CARTDETAIL_NOT_FOUND)) ;
    }

    @Override
    public void deleteCartDetail(Integer id) {
        CartDetail cartDetail = findById(id);
        cartDetailRepository.delete(cartDetail);
    }

    @Override
    public void deleteAllCartDetail(List<CartDetail> cartDetails) {
        cartDetailRepository.deleteAll(cartDetails);
    }

    @Override
    public CartDetail addCartDetail(Product product,Integer quantity,Cart cart) {
        CartDetail cartDetail=CartDetail.builder()
                .id(getGenerationId())
                .cart(cart).product(product)
                .unitPrice(product.getUnitPrice())
                .quantity(quantity)
                .totalPrice(product.getUnitPrice()*quantity)
                .build();
       return cartDetailRepository.save(cartDetail);
    }

    @Override
    public void updateCartDetail(CartDetail cartDetail,Integer quantity) {
      cartDetail.setQuantity(cartDetail.getQuantity()+quantity);
      cartDetail.setTotalPrice(cartDetail.getUnitPrice()*cartDetail.getQuantity());
      cartDetailRepository.save(cartDetail);
    }

    @Override
    public Optional<CartDetail> findByProductAndCart(Product product, Cart cart) {
        return cartDetailRepository.findByProductAndCart(product,cart);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
