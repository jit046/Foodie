package com.example.todo.service;

import com.example.todo.model.MenuItem;
import com.example.todo.model.Order;
import com.example.todo.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    /**
     * Create a new menu item
     */
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }
    
    /**
     * Get all menu items for a restaurant
     */
    public List<MenuItem> getMenuItemsByRestaurant(String restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);
    }
    
    /**
     * Get menu items by restaurant and meal type
     */
    public List<MenuItem> getMenuItemsByRestaurantAndMealType(String restaurantId, Order.MealType mealType) {
        return menuItemRepository.findByRestaurantIdAndMealTypesContaining(restaurantId, mealType.toString());
    }
    
    /**
     * Get menu item by ID
     */
    public Optional<MenuItem> getMenuItemById(String id) {
        return menuItemRepository.findById(id);
    }
    
    /**
     * Get menu items by category
     */
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }
    
    /**
     * Get menu items by restaurant and category
     */
    public List<MenuItem> getMenuItemsByRestaurantAndCategory(String restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category);
    }
    
    /**
     * Search menu items by name
     */
    public List<MenuItem> searchMenuItemsByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Search menu items by restaurant and name
     */
    public List<MenuItem> searchMenuItemsByRestaurantAndName(String restaurantId, String name) {
        return menuItemRepository.findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, name);
    }
    
    /**
     * Get menu items by price range
     */
    public List<MenuItem> getMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return menuItemRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * Get menu items by restaurant and price range
     */
    public List<MenuItem> getMenuItemsByRestaurantAndPriceRange(String restaurantId, BigDecimal minPrice, BigDecimal maxPrice) {
        return menuItemRepository.findByRestaurantIdAndPriceBetween(restaurantId, minPrice, maxPrice);
    }
    
    /**
     * Get vegetarian menu items
     */
    public List<MenuItem> getVegetarianMenuItems() {
        return menuItemRepository.findByIsVegetarianTrue();
    }
    
    /**
     * Get vegan menu items
     */
    public List<MenuItem> getVeganMenuItems() {
        return menuItemRepository.findByIsVeganTrue();
    }
    
    /**
     * Get spicy menu items
     */
    public List<MenuItem> getSpicyMenuItems() {
        return menuItemRepository.findByIsSpicyTrue();
    }
    
    /**
     * Get menu items by spice level
     */
    public List<MenuItem> getMenuItemsBySpiceLevel(int spiceLevel) {
        return menuItemRepository.findBySpiceLevel(spiceLevel);
    }
    
    /**
     * Get popular menu items
     */
    public List<MenuItem> getPopularMenuItems() {
        return menuItemRepository.findPopularMenuItems();
    }
    
    /**
     * Get menu items by tags
     */
    public List<MenuItem> getMenuItemsByTags(List<String> tags) {
        return menuItemRepository.findByTagsIn(tags);
    }
    
    /**
     * Get menu items by restaurant and tags
     */
    public List<MenuItem> getMenuItemsByRestaurantAndTags(String restaurantId, List<String> tags) {
        return menuItemRepository.findByRestaurantIdAndTagsIn(restaurantId, tags);
    }
    
    /**
     * Get menu items by preparation time
     */
    public List<MenuItem> getMenuItemsByPreparationTime(int maxPreparationTime) {
        return menuItemRepository.findByPreparationTimeLessThanEqual(maxPreparationTime);
    }
    
    /**
     * Get menu items by calories range
     */
    public List<MenuItem> getMenuItemsByCaloriesRange(int minCalories, int maxCalories) {
        return menuItemRepository.findByCaloriesBetween(minCalories, maxCalories);
    }
    
    /**
     * Update menu item
     */
    public MenuItem updateMenuItem(String id, MenuItem updatedMenuItem) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setName(updatedMenuItem.getName());
                    menuItem.setDescription(updatedMenuItem.getDescription());
                    menuItem.setPrice(updatedMenuItem.getPrice());
                    menuItem.setImageUrl(updatedMenuItem.getImageUrl());
                    menuItem.setAvailable(updatedMenuItem.isAvailable());
                    menuItem.setVegetarian(updatedMenuItem.isVegetarian());
                    menuItem.setVegan(updatedMenuItem.isVegan());
                    menuItem.setSpicy(updatedMenuItem.isSpicy());
                    menuItem.setSpiceLevel(updatedMenuItem.getSpiceLevel());
                    menuItem.setCategory(updatedMenuItem.getCategory());
                    menuItem.setMealTypes(updatedMenuItem.getMealTypes());
                    menuItem.setTags(updatedMenuItem.getTags());
                    menuItem.setCalories(updatedMenuItem.getCalories());
                    menuItem.setPreparationTime(updatedMenuItem.getPreparationTime());
                    menuItem.setServingSize(updatedMenuItem.getServingSize());
                    menuItem.setCustomizationOptions(updatedMenuItem.getCustomizationOptions());
                    
                    return menuItemRepository.save(menuItem);
                })
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }
    
    /**
     * Delete menu item (soft delete)
     */
    public void deleteMenuItem(String id) {
        menuItemRepository.findById(id)
                .ifPresent(menuItem -> {
                    menuItem.setAvailable(false);
                    menuItemRepository.save(menuItem);
                });
    }
    
    /**
     * Toggle menu item availability
     */
    public MenuItem toggleMenuItemAvailability(String id) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setAvailable(!menuItem.isAvailable());
                    return menuItemRepository.save(menuItem);
                })
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }
    
    /**
     * Get menu items by multiple criteria
     */
    public List<MenuItem> getMenuItemsByCriteria(String restaurantId, List<String> mealTypes, 
                                                BigDecimal minPrice, BigDecimal maxPrice, 
                                                String category, List<String> tags, 
                                                Boolean isVegetarian, Boolean isVegan, 
                                                Boolean isSpicy, Integer spiceLevel) {
        
        List<MenuItem> menuItems;
        
        if (restaurantId != null) {
            if (mealTypes != null && !mealTypes.isEmpty()) {
                menuItems = menuItemRepository.findAvailableMenuItemsByRestaurantAndMealTypes(restaurantId, mealTypes);
            } else {
                menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);
            }
        } else {
            menuItems = menuItemRepository.findAll();
        }
        
        // Apply filters
        if (minPrice != null && maxPrice != null) {
            menuItems = menuItems.stream()
                    .filter(item -> item.getPrice().compareTo(minPrice) >= 0 && item.getPrice().compareTo(maxPrice) <= 0)
                    .toList();
        }
        
        if (category != null) {
            menuItems = menuItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .toList();
        }
        
        if (tags != null && !tags.isEmpty()) {
            menuItems = menuItems.stream()
                    .filter(item -> item.getTags() != null && item.getTags().stream().anyMatch(tags::contains))
                    .toList();
        }
        
        if (isVegetarian != null) {
            menuItems = menuItems.stream()
                    .filter(item -> item.isVegetarian() == isVegetarian)
                    .toList();
        }
        
        if (isVegan != null) {
            menuItems = menuItems.stream()
                    .filter(item -> item.isVegan() == isVegan)
                    .toList();
        }
        
        if (isSpicy != null) {
            menuItems = menuItems.stream()
                    .filter(item -> item.isSpicy() == isSpicy)
                    .toList();
        }
        
        if (spiceLevel != null) {
            menuItems = menuItems.stream()
                    .filter(item -> item.getSpiceLevel() == spiceLevel)
                    .toList();
        }
        
        return menuItems;
    }
    
    /**
     * Get menu item statistics
     */
    public MenuItemStats getMenuItemStats(String menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .map(menuItem -> new MenuItemStats(
                        menuItem.getId(),
                        menuItem.getName(),
                        menuItem.getPrice(),
                        menuItem.isAvailable(),
                        menuItem.getPreparationTime(),
                        menuItem.getCalories()
                ))
                .orElse(null);
    }
    
    // Inner class for menu item statistics
    public static class MenuItemStats {
        private String id;
        private String name;
        private BigDecimal price;
        private boolean isAvailable;
        private int preparationTime;
        private int calories;
        
        public MenuItemStats(String id, String name, BigDecimal price, boolean isAvailable, 
                           int preparationTime, int calories) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.isAvailable = isAvailable;
            this.preparationTime = preparationTime;
            this.calories = calories;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }
        
        public int getPreparationTime() { return preparationTime; }
        public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
        
        public int getCalories() { return calories; }
        public void setCalories(int calories) { this.calories = calories; }
    }
}
