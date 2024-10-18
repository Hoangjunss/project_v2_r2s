package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductApplicationService productApplicationService;
    @PostMapping()
    public ResponseEntity<?> addProduct(@ModelAttribute @Valid ProductCreateDTO productCreateDTO) throws IOException {
        return new ResponseEntity<>(productApplicationService.createProduct(productCreateDTO), HttpStatus.CREATED);
    }
}
