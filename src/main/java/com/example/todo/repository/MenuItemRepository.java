package com.example.todo.repository;

import com.example.todo.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    
    // Find menu items by restaurant
    List<MenuItem> findByRestaurantId(String restaurantId);
    
    // Find available menu items by restaurant
    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(String restaurantId);
    
    // Find menu items by category
    List<MenuItem> findByCategory(String category);
    
    // Find menu items by meal type
    List<MenuItem> findByMealTypesContaining(String mealType);
    
    // Find menu items by restaurant and meal type
    List<MenuItem> findByRestaurantIdAndMealTypesContaining(String restaurantId, String mealType);
    
    // Find menu items by price range
    List<MenuItem> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find menu items by restaurant and price range
    List<MenuItem> findByRestaurantIdAndPriceBetween(String restaurantId, BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find vegetarian menu items
    List<MenuItem> findByIsVegetarianTrue();
    
    // Find vegan menu items
    List<MenuItem> findByIsVeganTrue();
    
    // Find spicy menu items
    List<MenuItem> findByIsSpicyTrue();
    
    // Find menu items by spice level
    List<MenuItem> findBySpiceLevel(int spiceLevel);
    
    // Find menu items by tags
    List<MenuItem> findByTagsContaining(String tag);
    
    // Find menu items by name (case-insensitive)
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    // Find menu items by restaurant and name
    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCase(String restaurantId, String name);
    
    // Find menu items by multiple criteria
    @Query("{'restaurantId': ?0, 'isAvailable': true, 'mealTypes': {$in: ?1}, 'price': {$gte: ?2, $lte: ?3}}")
    List<MenuItem> findAvailableMenuItemsByRestaurantAndMealTypeAndPriceRange(
            String restaurantId, List<String> mealTypes, BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find popular menu items (by tags)
    @Query("{'tags': 'Popular', 'isAvailable': true}")
    List<MenuItem> findPopularMenuItems();
    
    // Find menu items by restaurant and category
    List<MenuItem> findByRestaurantIdAndCategory(String restaurantId, String category);
    
    // Find menu items by preparation time
    List<MenuItem> findByPreparationTimeLessThanEqual(int maxPreparationTime);
    
    // Find menu items by calories range
    List<MenuItem> findByCaloriesBetween(int minCalories, int maxCalories);
    
    // Find menu items by serving size
    List<MenuItem> findByServingSize(int servingSize);
    
    // Find menu items by restaurant and availability
    @Query("{'restaurantId': ?0, 'isAvailable': true, 'mealTypes': {$in: ?1}}")
    List<MenuItem> findAvailableMenuItemsByRestaurantAndMealTypes(String restaurantId, List<String> mealTypes);
    
    // Find menu items by multiple tags
    @Query("{'tags': {$in: ?0}, 'isAvailable': true}")
    List<MenuItem> findByTagsIn(List<String> tags);
    
    // Find menu items by restaurant and tags
    @Query("{'restaurantId': ?0, 'tags': {$in: ?1}, 'isAvailable': true}")
    List<MenuItem> findByRestaurantIdAndTagsIn(String restaurantId, List<String> tags);
    
    // Find menu items by name and restaurant
    Optional<MenuItem> findByNameAndRestaurantId(String name, String restaurantId);
    
    // Count menu items by restaurant
    long countByRestaurantId(String restaurantId);
    
    // Count available menu items by restaurant
    long countByRestaurantIdAndIsAvailableTrue(String restaurantId);
}
