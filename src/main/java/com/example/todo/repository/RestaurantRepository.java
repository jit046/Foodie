package com.example.todo.repository;

import com.example.todo.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    
    // Find restaurants by name (case-insensitive)
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    
    // Find restaurants by category
    List<Restaurant> findByCategoriesContaining(String category);
    
    // Find restaurants by delivery area (pincode)
    List<Restaurant> findByDeliveryAreasContaining(String pincode);
    
    // Find active restaurants
    List<Restaurant> findByIsActiveTrue();
    
    // Find restaurants by rating range
    List<Restaurant> findByRatingGreaterThanEqual(double minRating);
    
    // Find restaurants by multiple criteria
    @Query("{'isActive': true, 'categories': {$in: ?0}, 'deliveryAreas': {$in: ?1}}")
    List<Restaurant> findActiveRestaurantsByCategoriesAndDeliveryAreas(List<String> categories, List<String> pincodes);
    
    // Find restaurants by name and location
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'deliveryAreas': {$in: ?1}}")
    List<Restaurant> findByNameAndDeliveryAreas(String name, List<String> pincodes);
    
    // Find restaurants with specific meal type availability
    @Query("{'isActive': true, 'deliveryAreas': {$in: ?0}, $or: [{'breakfastWindow': {$exists: true}}, {'lunchWindow': {$exists: true}}, {'dinnerWindow': {$exists: true}}]}")
    List<Restaurant> findActiveRestaurantsWithMealAvailability(List<String> pincodes);
    
    // Find restaurants by city
    @Query("{'address': {$regex: ?0, $options: 'i'}}")
    List<Restaurant> findByCity(String city);
    
    // Find restaurants with minimum rating and active status
    List<Restaurant> findByRatingGreaterThanEqualAndIsActiveTrue(double minRating);
    
    // Find restaurants by phone number
    Optional<Restaurant> findByPhoneNumber(String phoneNumber);
    
    // Find restaurants by email
    Optional<Restaurant> findByEmail(String email);
}
