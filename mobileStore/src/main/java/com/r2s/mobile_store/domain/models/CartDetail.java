package com.r2s.mobile_store.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class CartDetail {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn
    private Product product;
    @ManyToOne
    @JoinColumn
    private Cart cart;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
