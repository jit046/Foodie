package com.example.todo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String description;
    private String address;
    private String phoneNumber;
    private String email;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private boolean isActive;
    
    // Delivery time windows for each meal type
    private DeliveryTimeWindow breakfastWindow;
    private DeliveryTimeWindow lunchWindow;
    private DeliveryTimeWindow dinnerWindow;
    
    // Restaurant operating hours
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    // Delivery areas (pincodes)
    private List<String> deliveryAreas;
    
    // Restaurant categories
    private List<String> categories; // e.g., ["Indian", "Chinese", "Italian", "Fast Food"]
    
    public Restaurant() {}
    
    public Restaurant(String name, String description, String address, String phoneNumber, String email) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isActive = true;
        this.rating = 0.0;
        this.reviewCount = 0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public DeliveryTimeWindow getBreakfastWindow() { return breakfastWindow; }
    public void setBreakfastWindow(DeliveryTimeWindow breakfastWindow) { this.breakfastWindow = breakfastWindow; }
    
    public DeliveryTimeWindow getLunchWindow() { return lunchWindow; }
    public void setLunchWindow(DeliveryTimeWindow lunchWindow) { this.lunchWindow = lunchWindow; }
    
    public DeliveryTimeWindow getDinnerWindow() { return dinnerWindow; }
    public void setDinnerWindow(DeliveryTimeWindow dinnerWindow) { this.dinnerWindow = dinnerWindow; }
    
    public LocalTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }
    
    public LocalTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }
    
    public List<String> getDeliveryAreas() { return deliveryAreas; }
    public void setDeliveryAreas(List<String> deliveryAreas) { this.deliveryAreas = deliveryAreas; }
    
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    
    // Inner class for delivery time windows
    public static class DeliveryTimeWindow {
        private LocalTime orderStartTime;
        private LocalTime orderEndTime;
        private LocalTime deliveryStartTime;
        private LocalTime deliveryEndTime;
        
        public DeliveryTimeWindow() {}
        
        public DeliveryTimeWindow(LocalTime orderStartTime, LocalTime orderEndTime, 
                                LocalTime deliveryStartTime, LocalTime deliveryEndTime) {
            this.orderStartTime = orderStartTime;
            this.orderEndTime = orderEndTime;
            this.deliveryStartTime = deliveryStartTime;
            this.deliveryEndTime = deliveryEndTime;
        }
        
        // Getters and Setters
        public LocalTime getOrderStartTime() { return orderStartTime; }
        public void setOrderStartTime(LocalTime orderStartTime) { this.orderStartTime = orderStartTime; }
        
        public LocalTime getOrderEndTime() { return orderEndTime; }
        public void setOrderEndTime(LocalTime orderEndTime) { this.orderEndTime = orderEndTime; }
        
        public LocalTime getDeliveryStartTime() { return deliveryStartTime; }
        public void setDeliveryStartTime(LocalTime deliveryStartTime) { this.deliveryStartTime = deliveryStartTime; }
        
        public LocalTime getDeliveryEndTime() { return deliveryEndTime; }
        public void setDeliveryEndTime(LocalTime deliveryEndTime) { this.deliveryEndTime = deliveryEndTime; }
    }
}
