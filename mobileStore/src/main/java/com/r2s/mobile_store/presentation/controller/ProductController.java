package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductApplicationService productApplicationService;
    @PreAuthorize(" hasAuthority('ADMIN') ")
    @PostMapping()
    public ResponseEntity<?> addProduct(@ModelAttribute @Valid ProductCreateDTO productCreateDTO) throws IOException {
        return new ResponseEntity<>(productApplicationService.createProduct(productCreateDTO), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('USER')or hasRole('ADMIN') ")
    @GetMapping()
    public ResponseEntity<?> getAll( @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> getProductResponse=productApplicationService.getList(pageable);

        return ResponseEntity.ok(getProductResponse);
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN') ")
    @GetMapping("/id")
    public ResponseEntity<?> getId(@RequestParam Integer id ) {

      ProductDto productDto=productApplicationService.findById(id);

        return ResponseEntity.ok(productDto);
    }
}
