package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.Cart;
import com.r2s.mobile_store.domain.models.CartDetail;
import com.r2s.mobile_store.domain.models.Product;
import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.CartRepository;
import com.r2s.mobile_store.domain.service.CartDetailService;
import com.r2s.mobile_store.domain.service.CartService;
import com.r2s.mobile_store.domain.service.ProductService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private ProductService productService;

    @Override
    public Cart findById(Integer id) {
        return cartRepository.findById(id).orElseThrow(() -> new CustomException(Error.CART_NOT_FOUND));
    }

    @Override
    public Cart addCart( Integer idProduct, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User)) {
            throw new CustomException(Error.USER_NOT_FOUND);
        }
        User currentUser = (User) authentication.getPrincipal();

        Product product = productService.findById(idProduct);
        Optional<Cart> cartOptinal=cartRepository.findByUser(currentUser);
        Cart cart;
        if(!cartOptinal.isPresent()){
            cart= createCart(currentUser);
        }else {
            cart=cartOptinal.get();
        }
        Optional<CartDetail> optionalCartDetail = cartDetailService.findByProductAndCart(product, cart);

        CartDetail cartDetail;
        if (!optionalCartDetail.isPresent()) {
          cartDetail=  cartDetailService.addCartDetail(product,quantity,cart);
          cart.setQuantity(cartDetail.getQuantity());

        } else {

            cartDetail = optionalCartDetail.get();

            cartDetailService.updateCartDetailWhenAddProduct(product,cart,quantity);
            cart.setQuantity(cart.getQuantity()+quantity);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartDetail(Integer idProduct) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Product product = productService.findById(idProduct);
        Cart cart=cartRepository.findByUser(currentUser).orElseThrow(()->new CustomException(Error.CART_NOT_FOUND));


       CartDetail cartDetail= cartDetailService.deleteCartDetail(product,cart);


        cart.setQuantity(cart.getQuantity() - cartDetail.getQuantity());

        cartRepository.save(cart);

        return cart;
    }
@Transactional
    @Override
    public void clearCart() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User)) {
        throw new CustomException(Error.USER_NOT_FOUND);
    }
    User currentUser = (User) authentication.getPrincipal();

    Cart cart=cartRepository.findByUser(currentUser).orElseThrow(()->new CustomException(Error.CART_NOT_FOUND));
        cartDetailService.deleteAllCartDetail(cart);
      cartRepository.delete(cart);
    }

    @Override
    public Cart createCart(User user) {
        Cart cart=Cart.builder()
                .id(getGenerationId())
                .user(user)
                .quantity(0)
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public Cart findByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User)) {
            throw new CustomException(Error.USER_NOT_FOUND);
        }
        User currentUser = (User) authentication.getPrincipal();

        return cartRepository.findByUser(currentUser).orElse(null);
    }
@Transactional
    @Override
    public Cart updateCart(Integer idProduct, Integer quantity) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User)) {
        throw new CustomException(Error.USER_NOT_FOUND);
    }
    User currentUser = (User) authentication.getPrincipal();

    Cart cart=cartRepository.findByUser(currentUser).orElseThrow(()->new CustomException(Error.CART_NOT_FOUND));
        Product product=productService.findById(idProduct);
        cartDetailService.updateCartDetail(product,cart,quantity);
        return  cart;
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
