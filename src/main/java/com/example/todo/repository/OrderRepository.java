package com.example.todo.repository;

import com.example.todo.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    // Find orders by user
    List<Order> findByUserId(String userId);
    
    // Find orders by restaurant
    List<Order> findByRestaurantId(String restaurantId);
    
    // Find orders by status
    List<Order> findByStatus(Order.OrderStatus status);
    
    // Find orders by user and status
    List<Order> findByUserIdAndStatus(String userId, Order.OrderStatus status);
    
    // Find orders by restaurant and status
    List<Order> findByRestaurantIdAndStatus(String restaurantId, Order.OrderStatus status);
    
    // Find orders by meal type
    List<Order> findByMealType(Order.MealType mealType);
    
    // Find orders by user and meal type
    List<Order> findByUserIdAndMealType(String userId, Order.MealType mealType);
    
    // Find orders by order number
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // Find orders by payment status
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    // Find orders by user and payment status
    List<Order> findByUserIdAndPaymentStatus(String userId, Order.PaymentStatus paymentStatus);
    
    // Find orders by date range
    List<Order> findByOrderTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by user and date range
    List<Order> findByUserIdAndOrderTimeBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by restaurant and date range
    List<Order> findByRestaurantIdAndOrderTimeBetween(String restaurantId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by user, status, and date range
    List<Order> findByUserIdAndStatusAndOrderTimeBetween(String userId, Order.OrderStatus status, 
                                                        LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by restaurant, status, and date range
    List<Order> findByRestaurantIdAndStatusAndOrderTimeBetween(String restaurantId, Order.OrderStatus status, 
                                                              LocalDateTime startDate, LocalDateTime endDate);
    
    // Find orders by delivery person
    List<Order> findByDeliveryPersonPhone(String deliveryPersonPhone);
    
    // Find orders by delivery area (pincode)
    @Query("{'deliveryAddress.pincode': ?0}")
    List<Order> findByDeliveryPincode(String pincode);
    
    // Find orders by user and delivery area
    @Query("{'userId': ?0, 'deliveryAddress.pincode': ?1}")
    List<Order> findByUserIdAndDeliveryPincode(String userId, String pincode);
    
    // Find orders by total amount range
    @Query("{'totalAmount': {$gte: ?0, $lte: ?1}}")
    List<Order> findByTotalAmountBetween(double minAmount, double maxAmount);
    
    // Find orders by user and total amount range
    @Query("{'userId': ?0, 'totalAmount': {$gte: ?1, $lte: ?2}}")
    List<Order> findByUserIdAndTotalAmountBetween(String userId, double minAmount, double maxAmount);
    
    // Find orders by payment method
    List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod);
    
    // Find orders by user and payment method
    List<Order> findByUserIdAndPaymentMethod(String userId, Order.PaymentMethod paymentMethod);
    
    // Find orders by promo code
    List<Order> findByPromoCode(String promoCode);
    
    // Find orders by user and promo code
    List<Order> findByUserIdAndPromoCode(String userId, String promoCode);
    
    // Find recent orders by user (last N orders)
    @Query(value = "{'userId': ?0}", sort = "{'orderTime': -1}")
    List<Order> findRecentOrdersByUserId(String userId);
    
    // Find orders by multiple statuses
    @Query("{'status': {$in: ?0}}")
    List<Order> findByStatusIn(List<Order.OrderStatus> statuses);
    
    // Find orders by user and multiple statuses
    @Query("{'userId': ?0, 'status': {$in: ?1}}")
    List<Order> findByUserIdAndStatusIn(String userId, List<Order.OrderStatus> statuses);
    
    // Find orders by restaurant and multiple statuses
    @Query("{'restaurantId': ?0, 'status': {$in: ?1}}")
    List<Order> findByRestaurantIdAndStatusIn(String restaurantId, List<Order.OrderStatus> statuses);
    
    // Find orders by scheduled delivery time range
    @Query("{'scheduledDeliveryTime': {$gte: ?0, $lte: ?1}}")
    List<Order> findByScheduledDeliveryTimeBetween(String startTime, String endTime);
    
    // Find orders by user and scheduled delivery time range
    @Query("{'userId': ?0, 'scheduledDeliveryTime': {$gte: ?1, $lte: ?2}}")
    List<Order> findByUserIdAndScheduledDeliveryTimeBetween(String userId, String startTime, String endTime);
    
    // Count orders by user
    long countByUserId(String userId);
    
    // Count orders by restaurant
    long countByRestaurantId(String restaurantId);
    
    // Count orders by status
    long countByStatus(Order.OrderStatus status);
    
    // Count orders by user and status
    long countByUserIdAndStatus(String userId, Order.OrderStatus status);
    
    // Count orders by restaurant and status
    long countByRestaurantIdAndStatus(String restaurantId, Order.OrderStatus status);
    
    // Count orders by meal type
    long countByMealType(Order.MealType mealType);
    
    // Count orders by user and meal type
    long countByUserIdAndMealType(String userId, Order.MealType mealType);
}
