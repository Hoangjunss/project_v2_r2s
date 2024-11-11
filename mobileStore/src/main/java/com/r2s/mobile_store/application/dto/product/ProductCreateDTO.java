package com.r2s.mobile_store.application.dto.product;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Product name must not be blank")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private Double unitPrice;

    @NotNull(message = "Unit stock is required")  // Sử dụng @NotNull thay cho @NotBlank
    @Min(value = 0, message = "Unit stock must be greater than or equal to 0")
    private Integer unitStock;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private Integer manufacturer;
    private Integer category;
    private Integer condition;

    @NotNull(message = "File is required")
    private MultipartFile url;
}
