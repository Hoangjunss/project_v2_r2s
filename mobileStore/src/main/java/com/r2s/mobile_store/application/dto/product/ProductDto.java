package com.r2s.mobile_store.application.dto.product;

import com.r2s.mobile_store.domain.models.Category;
import com.r2s.mobile_store.domain.models.Condition;
import com.r2s.mobile_store.domain.models.Manufacturer;
import com.r2s.mobile_store.domain.models.ProductImage;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer id;
    private String productName;
    private Double unitPrice;
    private Integer unitStock;
    private String description;
    private String manufacturer;
    private String category;
    private String condition;
    private String url;
}
