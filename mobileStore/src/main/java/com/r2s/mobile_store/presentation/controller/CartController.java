package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.order.CartDTO;
import com.r2s.mobile_store.application.service.CartApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
