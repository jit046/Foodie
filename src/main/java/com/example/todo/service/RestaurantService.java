package com.example.todo.service;

import com.example.todo.model.Restaurant;
import com.example.todo.model.Order;
import com.example.todo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private TimeBasedOrderingService timeBasedOrderingService;
    
    /**
     * Create a new restaurant with default time windows
     */
    public Restaurant createRestaurant(Restaurant restaurant) {
        // Set default time windows for all meal types
        setDefaultTimeWindows(restaurant);
        return restaurantRepository.save(restaurant);
    }
    
    /**
     * Get all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }
    
    /**
     * Get restaurant by ID
     */
    public Optional<Restaurant> getRestaurantById(String id) {
        return restaurantRepository.findById(id);
    }
    
    /**
     * Get restaurants by category
     */
    public List<Restaurant> getRestaurantsByCategory(String category) {
        return restaurantRepository.findByCategoriesContaining(category);
    }
    
    /**
     * Get restaurants by delivery area
     */
    public List<Restaurant> getRestaurantsByDeliveryArea(String pincode) {
        return restaurantRepository.findByDeliveryAreasContaining(pincode);
    }
    
    /**
     * Get restaurants available for a specific meal type
     */
    public List<Restaurant> getRestaurantsForMealType(Order.MealType mealType) {
        List<Restaurant> allRestaurants = restaurantRepository.findByIsActiveTrue();
        return allRestaurants.stream()
                .filter(restaurant -> timeBasedOrderingService.isRestaurantAvailableForMeal(restaurant, mealType))
                .toList();
    }
    
    /**
     * Search restaurants by name
     */
    public List<Restaurant> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Search restaurants by name and location
     */
    public List<Restaurant> searchRestaurantsByNameAndLocation(String name, List<String> pincodes) {
        return restaurantRepository.findByNameAndDeliveryAreas(name, pincodes);
    }
    
    /**
     * Get restaurants by rating
     */
    public List<Restaurant> getRestaurantsByRating(double minRating) {
        return restaurantRepository.findByRatingGreaterThanEqualAndIsActiveTrue(minRating);
    }
    
    /**
     * Update restaurant
     */
    public Restaurant updateRestaurant(String id, Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(updatedRestaurant.getName());
                    restaurant.setDescription(updatedRestaurant.getDescription());
                    restaurant.setAddress(updatedRestaurant.getAddress());
                    restaurant.setPhoneNumber(updatedRestaurant.getPhoneNumber());
                    restaurant.setEmail(updatedRestaurant.getEmail());
                    restaurant.setImageUrl(updatedRestaurant.getImageUrl());
                    restaurant.setCategories(updatedRestaurant.getCategories());
                    restaurant.setDeliveryAreas(updatedRestaurant.getDeliveryAreas());
                    restaurant.setOpeningTime(updatedRestaurant.getOpeningTime());
                    restaurant.setClosingTime(updatedRestaurant.getClosingTime());
                    
                    // Update time windows if provided
                    if (updatedRestaurant.getBreakfastWindow() != null) {
                        restaurant.setBreakfastWindow(updatedRestaurant.getBreakfastWindow());
                    }
                    if (updatedRestaurant.getLunchWindow() != null) {
                        restaurant.setLunchWindow(updatedRestaurant.getLunchWindow());
                    }
                    if (updatedRestaurant.getDinnerWindow() != null) {
                        restaurant.setDinnerWindow(updatedRestaurant.getDinnerWindow());
                    }
                    
                    return restaurantRepository.save(restaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }
    
    /**
     * Delete restaurant (soft delete)
     */
    public void deleteRestaurant(String id) {
        restaurantRepository.findById(id)
                .ifPresent(restaurant -> {
                    restaurant.setActive(false);
                    restaurantRepository.save(restaurant);
                });
    }
    
    /**
     * Update restaurant rating
     */
    public Restaurant updateRestaurantRating(String id, double newRating) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    // Calculate new average rating
                    double currentRating = restaurant.getRating();
                    int currentReviewCount = restaurant.getReviewCount();
                    double newAverageRating = ((currentRating * currentReviewCount) + newRating) / (currentReviewCount + 1);
                    
                    restaurant.setRating(newAverageRating);
                    restaurant.setReviewCount(currentReviewCount + 1);
                    
                    return restaurantRepository.save(restaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }
    
    /**
     * Get restaurants by multiple criteria
     */
    public List<Restaurant> getRestaurantsByCriteria(List<String> categories, List<String> pincodes, 
                                                    Double minRating, Order.MealType mealType) {
        List<Restaurant> restaurants;
        
        if (categories != null && !categories.isEmpty() && pincodes != null && !pincodes.isEmpty()) {
            restaurants = restaurantRepository.findActiveRestaurantsByCategoriesAndDeliveryAreas(categories, pincodes);
        } else if (categories != null && !categories.isEmpty()) {
            restaurants = restaurantRepository.findByCategoriesContaining(categories.get(0));
        } else if (pincodes != null && !pincodes.isEmpty()) {
            restaurants = restaurantRepository.findByDeliveryAreasContaining(pincodes.get(0));
        } else {
            restaurants = restaurantRepository.findByIsActiveTrue();
        }
        
        // Filter by rating if specified
        if (minRating != null) {
            restaurants = restaurants.stream()
                    .filter(restaurant -> restaurant.getRating() >= minRating)
                    .toList();
        }
        
        // Filter by meal type availability if specified
        if (mealType != null) {
            restaurants = restaurants.stream()
                    .filter(restaurant -> timeBasedOrderingService.isRestaurantAvailableForMeal(restaurant, mealType))
                    .toList();
        }
        
        return restaurants;
    }
    
    /**
     * Set default time windows for a restaurant
     */
    private void setDefaultTimeWindows(Restaurant restaurant) {
        // Breakfast: Order 9 PM - 7:30 AM, Deliver 10 AM - 11 AM
        Restaurant.DeliveryTimeWindow breakfastWindow = new Restaurant.DeliveryTimeWindow(
                LocalTime.of(21, 0), LocalTime.of(7, 30),
                LocalTime.of(10, 0), LocalTime.of(11, 0)
        );
        restaurant.setBreakfastWindow(breakfastWindow);
        
        // Lunch: Order 6 AM - 10 AM, Deliver 1:15 PM - 2:00 PM
        Restaurant.DeliveryTimeWindow lunchWindow = new Restaurant.DeliveryTimeWindow(
                LocalTime.of(6, 0), LocalTime.of(10, 0),
                LocalTime.of(13, 15), LocalTime.of(14, 0)
        );
        restaurant.setLunchWindow(lunchWindow);
        
        // Dinner: Order 12 PM - 5 PM, Deliver 7:30 PM - 8:15 PM
        Restaurant.DeliveryTimeWindow dinnerWindow = new Restaurant.DeliveryTimeWindow(
                LocalTime.of(12, 0), LocalTime.of(17, 0),
                LocalTime.of(19, 30), LocalTime.of(20, 15)
        );
        restaurant.setDinnerWindow(dinnerWindow);
    }
    
    /**
     * Check if restaurant is currently accepting orders for a meal type
     */
    public boolean isRestaurantAcceptingOrders(String restaurantId, Order.MealType mealType) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> timeBasedOrderingService.isRestaurantAvailableForMeal(restaurant, mealType) &&
                                  timeBasedOrderingService.isOrderingAllowed(mealType))
                .orElse(false);
    }
    
    /**
     * Get restaurant statistics
     */
    public RestaurantStats getRestaurantStats(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> new RestaurantStats(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getRating(),
                        restaurant.getReviewCount(),
                        restaurant.isActive()
                ))
                .orElse(null);
    }
    
    // Inner class for restaurant statistics
    public static class RestaurantStats {
        private String id;
        private String name;
        private double rating;
        private int reviewCount;
        private boolean isActive;
        
        public RestaurantStats(String id, String name, double rating, int reviewCount, boolean isActive) {
            this.id = id;
            this.name = name;
            this.rating = rating;
            this.reviewCount = reviewCount;
            this.isActive = isActive;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        
        public int getReviewCount() { return reviewCount; }
        public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
        
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
    }
}
