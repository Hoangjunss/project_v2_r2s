package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.cart.CartDTO;
import com.r2s.mobile_store.application.dto.order.OrderDTO;
import com.r2s.mobile_store.application.service.CartApplicationService;
import com.r2s.mobile_store.infrastructure.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cart")
@RestController
public class CartController {
    @Autowired
    private CartApplicationService cartApplicationService;
    @PostMapping()
    public ResponseEntity<ApiResponse<CartDTO>> addProductToCart(
            @RequestParam Integer idProduct,
            @RequestParam Integer quantity,
            HttpServletRequest request) {

        CartDTO cartDTO = cartApplicationService.addCart( idProduct, quantity);
        ApiResponse<CartDTO> response = new ApiResponse<>(
                "success",
                "Cart add successfully",
                cartDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa chi tiết giỏ hàng
    @DeleteMapping("/{idProduct}")
    public ResponseEntity<ApiResponse<CartDTO>> deleteCartDetail(@PathVariable Integer idProduct, HttpServletRequest request) {
        CartDTO cartDTO = cartApplicationService.deleteCartDetail(idProduct);
        ApiResponse<CartDTO> response = new ApiResponse<>(
                "success",
                "Cart detail delete successfully",
                cartDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Làm trống giỏ hàng
    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> clearCart( HttpServletRequest request) {
        cartApplicationService.clearCart();
        ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Cart clear successfully",
                null,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Tìm kiếm giỏ hàng theo ID
    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> getCartById(HttpServletRequest request) {
        CartDTO cartDTO = cartApplicationService.findCart();

        // Xác định thông báo và dữ liệu dựa trên kết quả tìm kiếm Cart
        String message = cartDTO == null ? "Cart not found" : "Cart retrieved successfully";
        Object data = cartDTO == null ? "Cart not found" : cartDTO;

        ApiResponse<Object> response = new ApiResponse<>(
                "success",
                message,
                data,
                null,
                request.getRequestURI()
        );

        HttpStatus status = cartDTO == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<CartDTO>> updateCartDetail(
            @RequestParam Integer idProduct,
            @RequestParam Integer quantity,
            HttpServletRequest request) {
        CartDTO cartDTO = cartApplicationService.updateCart(idProduct, quantity);
        ApiResponse<CartDTO> response = new ApiResponse<>(
                "success",
                "Cart update successfully",
                cartDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
