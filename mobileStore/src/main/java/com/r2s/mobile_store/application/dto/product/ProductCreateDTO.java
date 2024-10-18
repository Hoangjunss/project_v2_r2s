package com.r2s.mobile_store.application.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {
    private String productName;
    private Double unitPrice;
    private Integer unitStock;
    private String description;
    private Integer manufacturer;
    private Integer category;
    private Integer condition;
    private MultipartFile url;
}
