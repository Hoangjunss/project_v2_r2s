package com.r2s.mobile_store.presentation.controller;


import com.r2s.mobile_store.application.dto.order.OrderCreateDTO;
import com.r2s.mobile_store.application.dto.order.OrderDTO;

import com.r2s.mobile_store.application.service.OrderApplicationService;
import com.r2s.mobile_store.infrastructure.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderApplicationService orderApplicationService;

    @PostMapping()
    public ResponseEntity<ApiResponse<OrderDTO>> addOrder( HttpServletRequest request) {
        OrderDTO orderDTO = orderApplicationService.addOrder();

        ApiResponse<OrderDTO> response = new ApiResponse<>(
                "success",
                "Order created successfully",
                orderDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

      @GetMapping()
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrderByUSer(HttpServletRequest request) {
        List<OrderDTO> orderDTO = orderApplicationService.getAll();

        ApiResponse<List<OrderDTO>> response = new ApiResponse<>(
                "success",
                "Order get successfully",
                orderDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderByID(@PathVariable Integer id, HttpServletRequest request) {
        OrderDTO orderDTO = orderApplicationService.findById(id);

        ApiResponse<OrderDTO> response = new ApiResponse<>(
                "success",
                "Order get successfully",
                orderDTO,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}