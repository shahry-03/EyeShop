package com.eyeshop.order.controller;

import com.eyeshop.order.entity.Order;
import com.eyeshop.order.entity.OrderItem;
import com.eyeshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    //CUSTOMER Methods
    //Place Order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestBody List<OrderItem> items){
        // Implementation will be done in service layer
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(userId, items));
    }

    // Cancel Order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id){
        // Implementation will be done in service layer
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }



    //Admin Methods
    // Get Order By Id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Get Orders By User Id
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

}
