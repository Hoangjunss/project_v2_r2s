package com.r2s.mobile_store.presentation.controller;


import com.r2s.mobile_store.domain.models.ProductImage;
import com.r2s.mobile_store.domain.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/image")
@RestController
public class ProjectImageController {
    @Autowired
    private ProductImageService productImageService;
    @GetMapping()
    public ResponseEntity<?> get(
            @RequestParam Integer id) {

        ProductImage image = productImageService.get(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getImageName() + "\"")
                .body(new ByteArrayResource(image.getImageData()));
    }
}
