package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.repository.CartDetailRepository;
import com.r2s.mobile_store.domain.repository.CartRepository;
import com.r2s.mobile_store.domain.repository.ProductRepository;
import com.r2s.mobile_store.domain.service.CartDetailService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public CartDetail findById(Integer integer) {
        return cartDetailRepository.findById(integer)
                .orElseThrow(()-> new CustomException(Error.CARTDETAIL_NOT_FOUND)) ;
    }

    @Override
    public CartDetail deleteCartDetail(Product product,Cart cart) {
        CartDetail cartDetail = cartDetailRepository.findByProductAndCart(product,cart).orElseThrow(()->new CustomException(Error.CARTDETAIL_NOT_FOUND));
        cartDetailRepository.delete(cartDetail);
        return  cartDetail;
    }

    @Override
    public void deleteAllCartDetail(Cart cart) {
        List<CartDetail> cartDetails=cartDetailRepository.findAllByCart(cart);
        cartDetailRepository.deleteAll(cartDetails);
    }
@Transactional
    @Override
    public CartDetail addCartDetail(Product product,Integer quantity,Cart cart) {
        if(product.getUnitStock()<quantity){
            throw  new CustomException(Error.PRODUCT_UNABLE_TO_STOCK);
        }
        CartDetail cartDetail=CartDetail.builder()
                .id(getGenerationId())
                .cart(cart).product(product)
                .quantity(quantity)
                .build();

       return cartDetailRepository.save(cartDetail);
    }
@Transactional
    @Override
    public void updateCartDetailWhenAddProduct(Product product,Cart cart,Integer quantity) {
        CartDetail cartDetail = cartDetailRepository.findByProductAndCart(product,cart).orElseThrow();
        if (cartDetail.getProduct().getUnitStock()<cartDetail.getQuantity()+quantity){
            throw  new CustomException(Error.CARTDETAIL_INVALID_QUANTITY);
        }
      cartDetail.setQuantity(cartDetail.getQuantity()+quantity);
      cartDetailRepository.save(cartDetail);
    }

    @Override
    public Optional<CartDetail> findByProductAndCart(Product product, Cart cart) {
        return cartDetailRepository.findByProductAndCart(product,cart);
    }

    @Override
    public List<CartDetail> findByCart(Integer cartId) {
        Cart cart =cartRepository.findById(cartId).orElseThrow();

        return cartDetailRepository.findAllByCart(cart);
    }
    @Transactional
    @Override
    public CartDetail updateCartDetail(Product product, Cart cart, Integer quantity) {
        CartDetail cartDetail=cartDetailRepository.findByProductAndCart(product, cart).orElseThrow(()->new CustomException(Error.CARTDETAIL_NOT_FOUND));
        if(quantity>product.getUnitStock()){
            throw  new CustomException(Error.CARTDETAIL_INVALID_QUANTITY);
        }
        int oldQuantity = cartDetail.getQuantity();

        // Trừ số lượng cũ khỏi tổng số lượng của Cart
        cart.setQuantity(cart.getQuantity()-oldQuantity);

        // Cập nhật CartDetail với số lượng mới
        cartDetail.setQuantity(quantity);


        // Thêm số lượng mới vào tổng số lượng của Cart
        cart.setQuantity(cart.getQuantity()+quantity);
        cartRepository.save(cart);
        return  cartDetailRepository.save(cartDetail);
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
