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

public class ProductImage {
    @Id
    private Integer id;
    private String imageName;
    private String imageType;

    @Lob
    @Column(columnDefinition = "LONGBLOB", name = "image_data")
    private byte[] imageData;
    private String url;
}
