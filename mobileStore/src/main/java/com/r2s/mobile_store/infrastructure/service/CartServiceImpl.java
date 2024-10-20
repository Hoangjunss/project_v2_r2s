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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
        User currentUser = (User) authentication.getPrincipal();

        Product product = productService.findById(idProduct);

        Cart cart=cartRepository.findByUser(currentUser);

        if (cart==null){
            cart=createCart(currentUser);
        }

        Optional<CartDetail> optionalCartDetail = cartDetailService.findByProductAndCart(product, cart);
        if (!(cart.getCartDetails() instanceof ArrayList)) {
            cart.setCartDetails(new ArrayList<>(cart.getCartDetails()));
        }
        if (product.getUnitStock() < quantity) {
            throw new CustomException(Error.PRODUCT_UNABLE_TO_STOCK);
        }

        CartDetail cartDetail;
        if (!optionalCartDetail.isPresent()) {

          cartDetail=  cartDetailService.addCartDetail(product,quantity,cart);


            cart.getCartDetails().add(cartDetail);
        } else {

            cartDetail = optionalCartDetail.get();

            cartDetailService.updateCartDetail(cartDetail,quantity);
        }
        cart.setQuantity(cart.getQuantity() + quantity);
        cart.setTotalPrice(cart.getTotalPrice() + product.getUnitPrice() * quantity);

        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartDetail(Integer idCartDetail) {
        CartDetail cartDetail = cartDetailService.findById(idCartDetail);


        Cart cart = cartDetail.getCart();


        cart.setQuantity(cart.getQuantity() - cartDetail.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() - cartDetail.getTotalPrice());
        if (cart.getQuantity() < 0) {
            cart.setQuantity(0);
        }
        if (cart.getTotalPrice() < 0) {
            cart.setTotalPrice(0.0);
        }

        List<CartDetail> modifiableCartDetails = new ArrayList<>(cart.getCartDetails());
        modifiableCartDetails.remove(cartDetail);
        cart.setCartDetails(modifiableCartDetails);


        cartDetailService.deleteCartDetail(idCartDetail);


        cartRepository.save(cart);

        return cart;
    }

    @Override
    public Cart clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        Cart cart = cartRepository.findByUser(currentUser);
        if (cart == null) {
            throw new CustomException(Error.CART_NOT_FOUND);
        }


        List<CartDetail> cartDetails = new ArrayList<>(cart.getCartDetails());


        if (!cartDetails.isEmpty()) {
            cartDetailService.deleteAllCartDetail(cartDetails);
        }


        cart.getCartDetails().clear();


        cart.setQuantity(0);
        cart.setTotalPrice(0.0);


        return cartRepository.save(cart);
    }


    @Override
    public Cart createCart(User user) {
        Cart cart=Cart.builder()
                .id(getGenerationId())
                .user(user)
                .totalPrice(Double.valueOf(0))
                .quantity(0)
                .cartDetails(new ArrayList<>())
                .build();
       return cartRepository.save(cart);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
