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
@Table(name = "product")
public class Product {
    @Id
    private Integer id;
    private String productName;
    private Double unitPrice;
    private int unitStock;
    private String description;
    @ManyToOne
    @JoinColumn(name="idManufacturer")
    private Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name="idCategory")
    private Category category;
    @ManyToOne
    @JoinColumn(name="idCondition")
    private Condition condition;
    @ManyToOne
    @JoinColumn(name="idProductImage")
    private ProductImage productImage;
}
