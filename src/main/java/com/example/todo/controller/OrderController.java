package com.example.todo.controller;

import com.example.todo.model.Order;
import com.example.todo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Create a new order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get order by order number
     */
    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<Order> order = orderService.getOrderByOrderNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get orders by user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by restaurant
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(@PathVariable String restaurantId) {
        List<Order> orders = orderService.getOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by user and status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByUserAndStatus(
            @PathVariable String userId, 
            @PathVariable Order.OrderStatus status) {
        
        List<Order> orders = orderService.getOrdersByUserAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by restaurant and status
     */
    @GetMapping("/restaurant/{restaurantId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByRestaurantAndStatus(
            @PathVariable String restaurantId, 
            @PathVariable Order.OrderStatus status) {
        
        List<Order> orders = orderService.getOrdersByRestaurantAndStatus(restaurantId, status);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by meal type
     */
    @GetMapping("/meal-type/{mealType}")
    public ResponseEntity<List<Order>> getOrdersByMealType(@PathVariable Order.MealType mealType) {
        List<Order> orders = orderService.getOrdersByMealType(mealType);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get recent orders by user
     */
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<Order>> getRecentOrdersByUser(@PathVariable String userId) {
        List<Order> orders = orderService.getRecentOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Update order status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String id, 
            @RequestParam Order.OrderStatus status) {
        
        try {
            Order order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Cancel order
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String id, 
            @RequestParam(required = false) String reason) {
        
        try {
            Order order = orderService.cancelOrder(id, reason);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update order payment status
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Order> updateOrderPaymentStatus(
            @PathVariable String id, 
            @RequestParam Order.PaymentStatus paymentStatus,
            @RequestParam(required = false) String transactionId) {
        
        try {
            Order order = orderService.updateOrderPaymentStatus(id, paymentStatus, transactionId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Assign delivery person to order
     */
    @PostMapping("/{id}/assign-delivery")
    public ResponseEntity<Order> assignDeliveryPerson(
            @PathVariable String id, 
            @RequestParam String deliveryPersonName,
            @RequestParam String deliveryPersonPhone) {
        
        try {
            Order order = orderService.assignDeliveryPerson(id, deliveryPersonName, deliveryPersonPhone);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get orders by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by user and date range
     */
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<Order>> getOrdersByUserAndDateRange(
            @PathVariable String userId,
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        
        List<Order> orders = orderService.getOrdersByUserAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by restaurant and date range
     */
    @GetMapping("/restaurant/{restaurantId}/date-range")
    public ResponseEntity<List<Order>> getOrdersByRestaurantAndDateRange(
            @PathVariable String restaurantId,
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        
        List<Order> orders = orderService.getOrdersByRestaurantAndDateRange(restaurantId, startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by delivery person
     */
    @GetMapping("/delivery-person/{deliveryPersonPhone}")
    public ResponseEntity<List<Order>> getOrdersByDeliveryPerson(@PathVariable String deliveryPersonPhone) {
        List<Order> orders = orderService.getOrdersByDeliveryPerson(deliveryPersonPhone);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by delivery area
     */
    @GetMapping("/delivery-area/{pincode}")
    public ResponseEntity<List<Order>> getOrdersByDeliveryArea(@PathVariable String pincode) {
        List<Order> orders = orderService.getOrdersByDeliveryArea(pincode);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get order statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<OrderService.OrderStats> getOrderStats(@PathVariable String id) {
        OrderService.OrderStats stats = orderService.getOrderStats(id);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }
    
    /**
     * Get user order statistics
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<OrderService.UserOrderStats> getUserOrderStats(@PathVariable String userId) {
        OrderService.UserOrderStats stats = orderService.getUserOrderStats(userId);
        return ResponseEntity.ok(stats);
    }
}
