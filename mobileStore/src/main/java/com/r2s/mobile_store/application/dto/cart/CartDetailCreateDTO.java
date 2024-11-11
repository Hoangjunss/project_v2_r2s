package com.r2s.mobile_store.application.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailCreateDTO {
    private Integer quantity;
    private Integer productId;
}
