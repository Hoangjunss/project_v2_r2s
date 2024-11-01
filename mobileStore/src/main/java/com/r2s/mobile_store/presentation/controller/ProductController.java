package com.r2s.mobile_store.presentation.controller;

import com.r2s.mobile_store.application.dto.product.ProductCreateDTO;
import com.r2s.mobile_store.application.dto.product.ProductDto;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.ProductApplicationService;
import com.r2s.mobile_store.infrastructure.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<ApiResponse<ProductDto>> addProduct(@ModelAttribute @Valid ProductCreateDTO productCreateDTO,
                                                              HttpServletRequest request) throws IOException {
        ProductDto productDto = productApplicationService.createProduct(productCreateDTO);
        ApiResponse<ProductDto> response = new ApiResponse<>(
                "success",
                "Product created successfully",
                productDto,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER') or hashasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) String search,
                                                                HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> productPage = productApplicationService.getList(search, pageable);

        // Determine data and message based on search result
        Object data = productPage.isEmpty() ? "Product not found" : productPage;
        String message = productPage.isEmpty() ? "No products found" : "Product list retrieved successfully";

        ApiResponse<Object> response = new ApiResponse<>(
                "success",
                message,
                data,
                null,
                request.getRequestURI()
        );


        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/id")
    public ResponseEntity<ApiResponse<ProductDto>> getId(@RequestParam Integer id, HttpServletRequest request) {
        ProductDto productDto = productApplicationService.findById(id);

        ApiResponse<ProductDto> response = new ApiResponse<>(
                "success",
                "Product retrieved successfully",
                productDto,
                null,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }
}