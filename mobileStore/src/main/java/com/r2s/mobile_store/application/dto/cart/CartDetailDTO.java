package com.r2s.mobile_store.application.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDTO {
    private Integer id;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
