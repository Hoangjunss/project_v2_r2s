package com.r2s.mobile_store.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Cart {
    @Id
    private  Integer id;
    @OneToOne
    @JoinColumn
    private User user;
    private Integer quantity;
    private Double totalPrice;

}
