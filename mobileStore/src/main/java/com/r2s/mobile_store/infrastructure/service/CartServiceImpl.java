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
        Optional<CartDetail> optionalCartDetail = cartDetailService.findByProductAndCart(product, cart);

        CartDetail cartDetail;
        if (!optionalCartDetail.isPresent()) {

          cartDetail=  cartDetailService.addCartDetail(product,quantity,cart);

            // Thêm CartDetail vào danh sách chi tiết của Cart
            cart.getCartDetails().add(cartDetail);
        } else {
            // Nếu đã có CartDetail, chỉ cập nhật số lượng
            cartDetail = optionalCartDetail.get();

            cartDetailService.updateCartDetail(cartDetail,quantity);
        }
        cart.setQuantity(cart.getQuantity() + quantity);  // Tăng số lượng tổng trong Cart
        cart.setTotalPrice(cart.getTotalPrice() + product.getUnitPrice() * quantity);

        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartDetail(Integer idCartDetail) {
        CartDetail cartDetail = cartDetailService.findById(idCartDetail);

        // Lấy Cart mà CartDetail thuộc về
        Cart cart = cartDetail.getCart();

        // Trừ số lượng và giá trị của CartDetail ra khỏi tổng trong Cart
        cart.setQuantity(cart.getQuantity() - cartDetail.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() - cartDetail.getTotalPrice());

        // Xóa CartDetail khỏi Cart
        cart.getCartDetails().remove(cartDetail);

        // Xóa CartDetail khỏi cơ sở dữ liệu
        cartDetailService.deleteCartDetail(idCartDetail);

        // Cập nhật lại Cart trong cơ sở dữ liệu
        cartRepository.save(cart);

        return cart;
    }

    @Override
    public Cart clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Cart cart=cartRepository.findByUser(currentUser);


        // Xóa tất cả CartDetail khỏi Cart
        List<CartDetail> cartDetails = cart.getCartDetails();
        cart.getCartDetails().clear();
        cartDetailService.deleteAllCartDetail(cartDetails);

        // Đặt lại số lượng và tổng giá trị trong Cart
        cart.setQuantity(0);
        cart.setTotalPrice(0.0);

        // Cập nhật lại Cart trong cơ sở dữ liệu
       return cartRepository.save(cart);
    }

    @Override
    public void createCart(User user) {
        Cart cart=Cart.builder()
                .id(getGenerationId())
                .user(user)
                .totalPrice(Double.valueOf(0))
                .quantity(0)
                .build();
        cartRepository.save(cart);
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
