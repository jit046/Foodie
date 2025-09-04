package com.example.todo.service;

import com.example.todo.model.Order;
import com.example.todo.model.MenuItem;
import com.example.todo.model.Restaurant;
import com.example.todo.repository.OrderRepository;
import com.example.todo.repository.MenuItemRepository;
import com.example.todo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private TimeBasedOrderingService timeBasedOrderingService;
    
    /**
     * Create a new order
     */
    public Order createOrder(Order order) {
        // Validate ordering is allowed for the meal type
        if (!timeBasedOrderingService.isOrderingAllowed(order.getMealType())) {
            throw new RuntimeException("Ordering is not allowed for " + order.getMealType() + " at this time");
        }
        
        // Validate restaurant is available for the meal type
        if (!timeBasedOrderingService.isRestaurantAvailableForMeal(
                restaurantRepository.findById(order.getRestaurantId()).orElse(null), order.getMealType())) {
            throw new RuntimeException("Restaurant is not available for " + order.getMealType());
        }
        
        // Generate order number
        order.setOrderNumber(generateOrderNumber());
        
        // Set scheduled delivery time
        LocalTime deliveryTime = timeBasedOrderingService.getDeliveryTime(order.getMealType());
        order.setScheduledDeliveryTime(deliveryTime);
        
        // Calculate order totals
        calculateOrderTotals(order);
        
        // Set order time
        order.setOrderTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }
    
    /**
     * Get order by order number
     */
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    /**
     * Get orders by user
     */
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * Get orders by restaurant
     */
    public List<Order> getOrdersByRestaurant(String restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
    
    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    /**
     * Get orders by user and status
     */
    public List<Order> getOrdersByUserAndStatus(String userId, Order.OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
    
    /**
     * Get orders by restaurant and status
     */
    public List<Order> getOrdersByRestaurantAndStatus(String restaurantId, Order.OrderStatus status) {
        return orderRepository.findByRestaurantIdAndStatus(restaurantId, status);
    }
    
    /**
     * Get orders by meal type
     */
    public List<Order> getOrdersByMealType(Order.MealType mealType) {
        return orderRepository.findByMealType(mealType);
    }
    
    /**
     * Get recent orders by user
     */
    public List<Order> getRecentOrdersByUser(String userId) {
        return orderRepository.findRecentOrdersByUserId(userId);
    }
    
    /**
     * Update order status
     */
    public Order updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(newStatus);
                    
                    // Set actual delivery time if order is delivered
                    if (newStatus == Order.OrderStatus.DELIVERED) {
                        order.setActualDeliveryTime(LocalDateTime.now());
                    }
                    
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    /**
     * Cancel order
     */
    public Order cancelOrder(String orderId, String reason) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    // Only allow cancellation if order is not already delivered or cancelled
                    if (order.getStatus() == Order.OrderStatus.DELIVERED || 
                        order.getStatus() == Order.OrderStatus.CANCELLED) {
                        throw new RuntimeException("Order cannot be cancelled");
                    }
                    
                    order.setStatus(Order.OrderStatus.CANCELLED);
                    order.setSpecialInstructions(reason);
                    
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    /**
     * Update order payment status
     */
    public Order updateOrderPaymentStatus(String orderId, Order.PaymentStatus paymentStatus, String transactionId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setPaymentStatus(paymentStatus);
                    order.setPaymentTransactionId(transactionId);
                    
                    // If payment is completed, confirm the order
                    if (paymentStatus == Order.PaymentStatus.COMPLETED && 
                        order.getStatus() == Order.OrderStatus.PENDING) {
                        order.setStatus(Order.OrderStatus.CONFIRMED);
                    }
                    
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    /**
     * Assign delivery person to order
     */
    public Order assignDeliveryPerson(String orderId, String deliveryPersonName, String deliveryPersonPhone) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setDeliveryPersonName(deliveryPersonName);
                    order.setDeliveryPersonPhone(deliveryPersonPhone);
                    order.setStatus(Order.OrderStatus.OUT_FOR_DELIVERY);
                    
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    /**
     * Get orders by date range
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderTimeBetween(startDate, endDate);
    }
    
    /**
     * Get orders by user and date range
     */
    public List<Order> getOrdersByUserAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByUserIdAndOrderTimeBetween(userId, startDate, endDate);
    }
    
    /**
     * Get orders by restaurant and date range
     */
    public List<Order> getOrdersByRestaurantAndDateRange(String restaurantId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByRestaurantIdAndOrderTimeBetween(restaurantId, startDate, endDate);
    }
    
    /**
     * Get orders by delivery person
     */
    public List<Order> getOrdersByDeliveryPerson(String deliveryPersonPhone) {
        return orderRepository.findByDeliveryPersonPhone(deliveryPersonPhone);
    }
    
    /**
     * Get orders by delivery area
     */
    public List<Order> getOrdersByDeliveryArea(String pincode) {
        return orderRepository.findByDeliveryPincode(pincode);
    }
    
    /**
     * Get order statistics
     */
    public OrderStats getOrderStats(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> new OrderStats(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getStatus(),
                        order.getTotalAmount(),
                        order.getOrderTime(),
                        order.getScheduledDeliveryTime(),
                        order.getActualDeliveryTime()
                ))
                .orElse(null);
    }
    
    /**
     * Get user order statistics
     */
    public UserOrderStats getUserOrderStats(String userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        
        long totalOrders = userOrders.size();
        long completedOrders = userOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .count();
        long cancelledOrders = userOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.CANCELLED)
                .count();
        
        BigDecimal totalSpent = userOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new UserOrderStats(userId, totalOrders, completedOrders, cancelledOrders, totalSpent);
    }
    
    /**
     * Calculate order totals
     */
    private void calculateOrderTotals(Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Calculate subtotal from order items
        if (order.getItems() != null) {
            for (Order.OrderItem item : order.getItems()) {
                subtotal = subtotal.add(item.getTotalPrice());
            }
        }
        
        order.setSubtotal(subtotal);
        
        // Calculate delivery fee (example: 30 rupees)
        order.setDeliveryFee(new BigDecimal("30.00"));
        
        // Calculate tax (example: 5% of subtotal)
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.05"));
        order.setTax(tax);
        
        // Calculate total amount
        BigDecimal total = subtotal.add(order.getDeliveryFee()).add(tax).subtract(order.getDiscountAmount());
        order.setTotalAmount(total);
    }
    
    /**
     * Generate unique order number
     */
    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    // Inner classes for statistics
    public static class OrderStats {
        private String id;
        private String orderNumber;
        private Order.OrderStatus status;
        private BigDecimal totalAmount;
        private LocalDateTime orderTime;
        private LocalTime scheduledDeliveryTime;
        private LocalDateTime actualDeliveryTime;
        
        public OrderStats(String id, String orderNumber, Order.OrderStatus status, BigDecimal totalAmount,
                         LocalDateTime orderTime, LocalTime scheduledDeliveryTime, LocalDateTime actualDeliveryTime) {
            this.id = id;
            this.orderNumber = orderNumber;
            this.status = status;
            this.totalAmount = totalAmount;
            this.orderTime = orderTime;
            this.scheduledDeliveryTime = scheduledDeliveryTime;
            this.actualDeliveryTime = actualDeliveryTime;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        
        public Order.OrderStatus getStatus() { return status; }
        public void setStatus(Order.OrderStatus status) { this.status = status; }
        
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        
        public LocalDateTime getOrderTime() { return orderTime; }
        public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
        
        public LocalTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
        public void setScheduledDeliveryTime(LocalTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
        
        public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
        public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
    }
    
    public static class UserOrderStats {
        private String userId;
        private long totalOrders;
        private long completedOrders;
        private long cancelledOrders;
        private BigDecimal totalSpent;
        
        public UserOrderStats(String userId, long totalOrders, long completedOrders, long cancelledOrders, BigDecimal totalSpent) {
            this.userId = userId;
            this.totalOrders = totalOrders;
            this.completedOrders = completedOrders;
            this.cancelledOrders = cancelledOrders;
            this.totalSpent = totalSpent;
        }
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
        
        public long getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(long completedOrders) { this.completedOrders = completedOrders; }
        
        public long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
        
        public BigDecimal getTotalSpent() { return totalSpent; }
        public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
    }
}
