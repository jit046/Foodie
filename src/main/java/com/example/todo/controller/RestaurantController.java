package com.example.todo.controller;

import com.example.todo.model.Order;
import com.example.todo.model.Restaurant;
import com.example.todo.service.RestaurantService;
import com.example.todo.service.TimeBasedOrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    @Autowired
    private RestaurantService restaurantService;
    
    @Autowired
    private TimeBasedOrderingService timeBasedOrderingService;
    
    /**
     * Get all restaurants
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurant by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable String id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new restaurant
     */
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }
    
    /**
     * Update restaurant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable String id, @RequestBody Restaurant restaurant) {
        try {
            Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, restaurant);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete restaurant
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Search restaurants by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String name) {
        List<Restaurant> restaurants = restaurantService.searchRestaurantsByName(name);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Search restaurants by name and location
     */
    @GetMapping("/search/location")
    public ResponseEntity<List<Restaurant>> searchRestaurantsByNameAndLocation(
            @RequestParam String name, 
            @RequestParam List<String> pincodes) {
        List<Restaurant> restaurants = restaurantService.searchRestaurantsByNameAndLocation(name, pincodes);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurants by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCategory(@PathVariable String category) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCategory(category);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurants by delivery area
     */
    @GetMapping("/delivery-area/{pincode}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByDeliveryArea(@PathVariable String pincode) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByDeliveryArea(pincode);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurants by rating
     */
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByRating(@PathVariable double minRating) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByRating(minRating);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurants available for a specific meal type
     */
    @GetMapping("/meal-type/{mealType}")
    public ResponseEntity<List<Restaurant>> getRestaurantsForMealType(@PathVariable Order.MealType mealType) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsForMealType(mealType);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Get restaurants by multiple criteria
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCriteria(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> pincodes,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Order.MealType mealType) {
        
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCriteria(categories, pincodes, minRating, mealType);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Check if restaurant is accepting orders for a meal type
     */
    @GetMapping("/{id}/accepting-orders")
    public ResponseEntity<Boolean> isRestaurantAcceptingOrders(
            @PathVariable String id, 
            @RequestParam Order.MealType mealType) {
        
        boolean isAccepting = restaurantService.isRestaurantAcceptingOrders(id, mealType);
        return ResponseEntity.ok(isAccepting);
    }
    
    /**
     * Get restaurant statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<RestaurantService.RestaurantStats> getRestaurantStats(@PathVariable String id) {
        RestaurantService.RestaurantStats stats = restaurantService.getRestaurantStats(id);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }
    
    /**
     * Update restaurant rating
     */
    @PostMapping("/{id}/rating")
    public ResponseEntity<Restaurant> updateRestaurantRating(
            @PathVariable String id, 
            @RequestParam double rating) {
        
        try {
            Restaurant restaurant = restaurantService.updateRestaurantRating(id, rating);
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get available ordering windows
     */
    @GetMapping("/ordering-windows")
    public ResponseEntity<List<TimeBasedOrderingService.OrderingWindow>> getAvailableOrderingWindows() {
        List<TimeBasedOrderingService.OrderingWindow> windows = timeBasedOrderingService.getAvailableOrderingWindows();
        return ResponseEntity.ok(windows);
    }
    
    /**
     * Get next ordering window for a meal type
     */
    @GetMapping("/ordering-windows/{mealType}")
    public ResponseEntity<TimeBasedOrderingService.OrderingWindow> getNextOrderingWindow(
            @PathVariable Order.MealType mealType) {
        
        TimeBasedOrderingService.OrderingWindow window = timeBasedOrderingService.getNextOrderingWindow(mealType);
        return window != null ? ResponseEntity.ok(window) : ResponseEntity.notFound().build();
    }
    
    /**
     * Check if ordering is allowed for a meal type
     */
    @GetMapping("/ordering-allowed/{mealType}")
    public ResponseEntity<Boolean> isOrderingAllowed(@PathVariable Order.MealType mealType) {
        boolean isAllowed = timeBasedOrderingService.isOrderingAllowed(mealType);
        return ResponseEntity.ok(isAllowed);
    }
    
    /**
     * Get time remaining for ordering a meal type
     */
    @GetMapping("/time-remaining/{mealType}")
    public ResponseEntity<TimeBasedOrderingService.TimeRemaining> getTimeRemainingForOrdering(
            @PathVariable Order.MealType mealType) {
        
        TimeBasedOrderingService.TimeRemaining timeRemaining = timeBasedOrderingService.getTimeRemainingForOrdering(mealType);
        return ResponseEntity.ok(timeRemaining);
    }
}
