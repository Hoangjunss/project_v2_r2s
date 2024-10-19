package com.r2s.mobile_store.infrastructure.service;

import com.r2s.mobile_store.domain.models.ProductImage;
import com.r2s.mobile_store.domain.repository.ProductImageRepository;
import com.r2s.mobile_store.domain.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;


@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;
    @Value("${image.service.url}")
    private String imageServiceUrl;
    @Override
    public ProductImage addProductImage(MultipartFile file) throws IOException {

        ProductImage productImage=ProductImage.builder()
                .id(getGenerationId())
                .imageName(file.getOriginalFilename())
                .imageType(file.getContentType())
                .imageData(file.getBytes())
                .build();
        productImage.setUrl(imageServiceUrl+"image?id="+productImage.getId());

        return productImageRepository.save(productImage);

    }
    @Override
    public ProductImage get(Integer id) {

        return productImageRepository.findById(id).orElseThrow();

    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}
