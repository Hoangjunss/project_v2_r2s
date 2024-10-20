package com.r2s.mobile_store.application.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;
    private String userName;
    private Integer quantity;
    private Double totalPrice;
    private List<CartDetailDTO> cartDetails;
}
