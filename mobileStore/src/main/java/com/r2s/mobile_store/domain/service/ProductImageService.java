package com.r2s.mobile_store.domain.service;

import com.r2s.mobile_store.domain.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductImageService {
    ProductImage addProductImage(MultipartFile productImage) throws IOException;
    ProductImage get(Integer id);
}
