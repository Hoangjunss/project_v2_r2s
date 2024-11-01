package com.r2s.mobile_store.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailCreateDTO {

    private Integer idProduct;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
