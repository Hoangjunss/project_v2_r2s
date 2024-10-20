package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.service.CartApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/cart")
@RestController
public class CartController {
    @Autowired
    private CartApplicationService cartApplicationService;
    @PostMapping()
    public ResponseEntity<CartDTO> addProductToCart(

            @RequestParam Integer idProduct,
            @RequestParam Integer quantity) {

        CartDTO cartDTO = cartApplicationService.addCart( idProduct, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    // Xóa chi tiết giỏ hàng
    @DeleteMapping("/detail")
    public ResponseEntity<CartDTO> deleteCartDetail(@RequestParam Integer idCartDetail) {
        CartDTO cartDTO = cartApplicationService.deleteCartDetail(idCartDetail);
        return ResponseEntity.ok(cartDTO);
    }

    // Làm trống giỏ hàng
    @DeleteMapping("/clear")
    public ResponseEntity<CartDTO> clearCart() {
        CartDTO cartDTO = cartApplicationService.clearCart();
        return ResponseEntity.ok(cartDTO);
    }

    // Tìm kiếm giỏ hàng theo ID
    @GetMapping()
    public ResponseEntity<CartDTO> getCartById(@RequestParam Integer idCart) {
        CartDTO cartDTO = cartApplicationService.findById(idCart);
        return ResponseEntity.ok(cartDTO);
    }

}
