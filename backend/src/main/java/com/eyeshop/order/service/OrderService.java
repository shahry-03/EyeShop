package com.eyeshop.order.service;

import com.eyeshop.order.entity.Order;
import com.eyeshop.order.entity.OrderItem;
import com.eyeshop.order.entity.Status;
import com.eyeshop.order.repository.OrderRepository;
import com.eyeshop.product.entity.Product;
import com.eyeshop.product.repository.ProductRepository;
import com.eyeshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // --- Place the Order
    @Transactional
    public Order placeOrder(Long userId, List<OrderItem> items){
        // Step1: user exists check
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        double totalPrice = 0;
        // Step2: check for each order item
        for(OrderItem item: items){
            // product exists check
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: "
                            + item.getProductId()));

            // stock check
            if(product.getStock() < item.getQuantity()){
                throw new RuntimeException("Insufficient stock for product: "
                        + product.getName() + ". Available stock: " + product.getStock());
            }

            // Set the price of item
            item.setPrice(product.getPrice() * item.getQuantity());

            // add in total price
            totalPrice += item.getPrice();

            // Reduce the stock
            product.setStock(product.getStock() - item.getQuantity());

        }
        //Step3: build the Order
        Order order = Order.builder()
                .userId(userId)
                .items(items)
                .totalPrice(totalPrice)
                .status(Status.PLACED)
                .build();

        //Step4: Link each item with Order
        for (OrderItem orderItem : items){
            orderItem.setOrder(order);
        }

        // save the order
        return orderRepository.save(order);

    }

    // ---Get Order By Id
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

    }

    // --- Get Orders by userId
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Cancel the order
    @Transactional
    public Order cancelOrder(Long orderId) {

        // First of all we find the order which we want to cancel
        Order order = getOrderById(orderId);
        //Already Cancelled check
        if (order.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }
        // Restore the Stock
        for (OrderItem orderItem : order.getItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: "+ orderItem.getProductId()));
            // Restore the stock of the product in each order item
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }

        //Update the status
        order.setStatus(Status.CANCELLED);
        return orderRepository.save(order);
    }
}
