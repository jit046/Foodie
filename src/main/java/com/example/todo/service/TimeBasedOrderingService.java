package com.example.todo.service;

import com.example.todo.model.Order;
import com.example.todo.model.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class TimeBasedOrderingService {
    
    // Time windows as per requirements
    private static final LocalTime BREAKFAST_ORDER_START = LocalTime.of(21, 0); // 9:00 PM
    private static final LocalTime BREAKFAST_ORDER_END = LocalTime.of(7, 30);   // 7:30 AM
    private static final LocalTime BREAKFAST_DELIVERY_START = LocalTime.of(10, 0);  // 10:00 AM
    private static final LocalTime BREAKFAST_DELIVERY_END = LocalTime.of(11, 0);    // 11:00 AM
    
    private static final LocalTime LUNCH_ORDER_START = LocalTime.of(6, 0);      // 6:00 AM
    private static final LocalTime LUNCH_ORDER_END = LocalTime.of(10, 0);       // 10:00 AM
    private static final LocalTime LUNCH_DELIVERY_START = LocalTime.of(13, 15); // 1:15 PM
    private static final LocalTime LUNCH_DELIVERY_END = LocalTime.of(14, 0);    // 2:00 PM
    
    private static final LocalTime DINNER_ORDER_START = LocalTime.of(12, 0);    // 12:00 PM
    private static final LocalTime DINNER_ORDER_END = LocalTime.of(17, 0);      // 5:00 PM
    private static final LocalTime DINNER_DELIVERY_START = LocalTime.of(19, 30); // 7:30 PM
    private static final LocalTime DINNER_DELIVERY_END = LocalTime.of(20, 15);   // 8:15 PM
    
    /**
     * Check if ordering is allowed for a specific meal type at the current time
     */
    public boolean isOrderingAllowed(Order.MealType mealType) {
        LocalTime currentTime = LocalTime.now();
        
        switch (mealType) {
            case BREAKFAST:
                return isWithinBreakfastOrderWindow(currentTime);
            case LUNCH:
                return isWithinLunchOrderWindow(currentTime);
            case DINNER:
                return isWithinDinnerOrderWindow(currentTime);
            default:
                return false;
        }
    }
    
    /**
     * Get the next available ordering window for a meal type
     */
    public OrderingWindow getNextOrderingWindow(Order.MealType mealType) {
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        
        switch (mealType) {
            case BREAKFAST:
                if (isWithinBreakfastOrderWindow(currentTime)) {
                    return new OrderingWindow(currentDate, BREAKFAST_ORDER_START, BREAKFAST_ORDER_END, 
                                            BREAKFAST_DELIVERY_START, BREAKFAST_DELIVERY_END);
                } else {
                    // Next breakfast window is tomorrow
                    return new OrderingWindow(currentDate.plusDays(1), BREAKFAST_ORDER_START, BREAKFAST_ORDER_END, 
                                            BREAKFAST_DELIVERY_START, BREAKFAST_DELIVERY_END);
                }
            case LUNCH:
                if (isWithinLunchOrderWindow(currentTime)) {
                    return new OrderingWindow(currentDate, LUNCH_ORDER_START, LUNCH_ORDER_END, 
                                            LUNCH_DELIVERY_START, LUNCH_DELIVERY_END);
                } else {
                    // Next lunch window is tomorrow
                    return new OrderingWindow(currentDate.plusDays(1), LUNCH_ORDER_START, LUNCH_ORDER_END, 
                                            LUNCH_DELIVERY_START, LUNCH_DELIVERY_END);
                }
            case DINNER:
                if (isWithinDinnerOrderWindow(currentTime)) {
                    return new OrderingWindow(currentDate, DINNER_ORDER_START, DINNER_ORDER_END, 
                                            DINNER_DELIVERY_START, DINNER_DELIVERY_END);
                } else {
                    // Next dinner window is tomorrow
                    return new OrderingWindow(currentDate.plusDays(1), DINNER_ORDER_START, DINNER_ORDER_END, 
                                            DINNER_DELIVERY_START, DINNER_DELIVERY_END);
                }
            default:
                return null;
        }
    }
    
    /**
     * Get all available ordering windows for today
     */
    public List<OrderingWindow> getAvailableOrderingWindows() {
        List<OrderingWindow> windows = new ArrayList<>();
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        
        // Check breakfast window
        if (isWithinBreakfastOrderWindow(currentTime)) {
            windows.add(new OrderingWindow(currentDate, BREAKFAST_ORDER_START, BREAKFAST_ORDER_END, 
                                        BREAKFAST_DELIVERY_START, BREAKFAST_DELIVERY_END));
        }
        
        // Check lunch window
        if (isWithinLunchOrderWindow(currentTime)) {
            windows.add(new OrderingWindow(currentDate, LUNCH_ORDER_START, LUNCH_ORDER_END, 
                                        LUNCH_DELIVERY_START, LUNCH_DELIVERY_END));
        }
        
        // Check dinner window
        if (isWithinDinnerOrderWindow(currentTime)) {
            windows.add(new OrderingWindow(currentDate, DINNER_ORDER_START, DINNER_ORDER_END, 
                                        DINNER_DELIVERY_START, DINNER_DELIVERY_END));
        }
        
        return windows;
    }
    
    /**
     * Get the delivery time for a specific meal type
     */
    public LocalTime getDeliveryTime(Order.MealType mealType) {
        switch (mealType) {
            case BREAKFAST:
                return BREAKFAST_DELIVERY_START;
            case LUNCH:
                return LUNCH_DELIVERY_START;
            case DINNER:
                return DINNER_DELIVERY_START;
            default:
                return null;
        }
    }
    
    /**
     * Get the delivery time range for a specific meal type
     */
    public DeliveryTimeRange getDeliveryTimeRange(Order.MealType mealType) {
        switch (mealType) {
            case BREAKFAST:
                return new DeliveryTimeRange(BREAKFAST_DELIVERY_START, BREAKFAST_DELIVERY_END);
            case LUNCH:
                return new DeliveryTimeRange(LUNCH_DELIVERY_START, LUNCH_DELIVERY_END);
            case DINNER:
                return new DeliveryTimeRange(DINNER_DELIVERY_START, DINNER_DELIVERY_END);
            default:
                return null;
        }
    }
    
    /**
     * Check if a restaurant is available for a specific meal type
     */
    public boolean isRestaurantAvailableForMeal(Restaurant restaurant, Order.MealType mealType) {
        if (restaurant == null || !restaurant.isActive()) {
            return false;
        }
        
        switch (mealType) {
            case BREAKFAST:
                return restaurant.getBreakfastWindow() != null;
            case LUNCH:
                return restaurant.getLunchWindow() != null;
            case DINNER:
                return restaurant.getDinnerWindow() != null;
            default:
                return false;
        }
    }
    
    /**
     * Get the time remaining for ordering a specific meal type
     */
    public TimeRemaining getTimeRemainingForOrdering(Order.MealType mealType) {
        LocalTime currentTime = LocalTime.now();
        
        switch (mealType) {
            case BREAKFAST:
                if (isWithinBreakfastOrderWindow(currentTime)) {
                    return calculateTimeRemaining(currentTime, BREAKFAST_ORDER_END);
                }
                break;
            case LUNCH:
                if (isWithinLunchOrderWindow(currentTime)) {
                    return calculateTimeRemaining(currentTime, LUNCH_ORDER_END);
                }
                break;
            case DINNER:
                if (isWithinDinnerOrderWindow(currentTime)) {
                    return calculateTimeRemaining(currentTime, DINNER_ORDER_END);
                }
                break;
        }
        
        return new TimeRemaining(0, 0, 0, false);
    }
    
    // Private helper methods
    private boolean isWithinBreakfastOrderWindow(LocalTime currentTime) {
        // Breakfast window spans from 9 PM to 7:30 AM next day
        return currentTime.isAfter(BREAKFAST_ORDER_START) || currentTime.isBefore(BREAKFAST_ORDER_END);
    }
    
    private boolean isWithinLunchOrderWindow(LocalTime currentTime) {
        return currentTime.isAfter(LUNCH_ORDER_START) && currentTime.isBefore(LUNCH_ORDER_END);
    }
    
    private boolean isWithinDinnerOrderWindow(LocalTime currentTime) {
        return currentTime.isAfter(DINNER_ORDER_START) && currentTime.isBefore(DINNER_ORDER_END);
    }
    
    private TimeRemaining calculateTimeRemaining(LocalTime currentTime, LocalTime endTime) {
        if (currentTime.isAfter(endTime)) {
            return new TimeRemaining(0, 0, 0, false);
        }
        
        int totalMinutes = endTime.toSecondOfDay() / 60 - currentTime.toSecondOfDay() / 60;
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        
        return new TimeRemaining(hours, minutes, 0, true);
    }
    
    // Inner classes for data transfer
    public static class OrderingWindow {
        private LocalDate date;
        private LocalTime orderStartTime;
        private LocalTime orderEndTime;
        private LocalTime deliveryStartTime;
        private LocalTime deliveryEndTime;
        private Order.MealType mealType;
        
        public OrderingWindow(LocalDate date, LocalTime orderStartTime, LocalTime orderEndTime, 
                            LocalTime deliveryStartTime, LocalTime deliveryEndTime) {
            this.date = date;
            this.orderStartTime = orderStartTime;
            this.orderEndTime = orderEndTime;
            this.deliveryStartTime = deliveryStartTime;
            this.deliveryEndTime = deliveryEndTime;
        }
        
        // Getters and Setters
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        
        public LocalTime getOrderStartTime() { return orderStartTime; }
        public void setOrderStartTime(LocalTime orderStartTime) { this.orderStartTime = orderStartTime; }
        
        public LocalTime getOrderEndTime() { return orderEndTime; }
        public void setOrderEndTime(LocalTime orderEndTime) { this.orderEndTime = orderEndTime; }
        
        public LocalTime getDeliveryStartTime() { return deliveryStartTime; }
        public void setDeliveryStartTime(LocalTime deliveryStartTime) { this.deliveryStartTime = deliveryStartTime; }
        
        public LocalTime getDeliveryEndTime() { return deliveryEndTime; }
        public void setDeliveryEndTime(LocalTime deliveryEndTime) { this.deliveryEndTime = deliveryEndTime; }
        
        public Order.MealType getMealType() { return mealType; }
        public void setMealType(Order.MealType mealType) { this.mealType = mealType; }
    }
    
    public static class DeliveryTimeRange {
        private LocalTime startTime;
        private LocalTime endTime;
        
        public DeliveryTimeRange(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        // Getters and Setters
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    }
    
    public static class TimeRemaining {
        private int hours;
        private int minutes;
        private int seconds;
        private boolean isAvailable;
        
        public TimeRemaining(int hours, int minutes, int seconds, boolean isAvailable) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.isAvailable = isAvailable;
        }
        
        // Getters and Setters
        public int getHours() { return hours; }
        public void setHours(int hours) { this.hours = hours; }
        
        public int getMinutes() { return minutes; }
        public void setMinutes(int minutes) { this.minutes = minutes; }
        
        public int getSeconds() { return seconds; }
        public void setSeconds(int seconds) { this.seconds = seconds; }
        
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }
    }
}
