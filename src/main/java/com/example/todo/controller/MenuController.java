package com.example.todo.controller;

import com.example.todo.model.MenuItem;
import com.example.todo.model.Order;
import com.example.todo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * Get all menu items for a restaurant
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurant(@PathVariable String restaurantId) {
        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by restaurant and meal type
     */
    @GetMapping("/restaurant/{restaurantId}/meal-type/{mealType}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurantAndMealType(
            @PathVariable String restaurantId, 
            @PathVariable Order.MealType mealType) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurantAndMealType(restaurantId, mealType);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable String id) {
        Optional<MenuItem> menuItem = menuService.getMenuItemById(id);
        return menuItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new menu item
     */
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem createdMenuItem = menuService.createMenuItem(menuItem);
        return ResponseEntity.ok(createdMenuItem);
    }
    
    /**
     * Update menu item
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String id, @RequestBody MenuItem menuItem) {
        try {
            MenuItem updatedMenuItem = menuService.updateMenuItem(id, menuItem);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete menu item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Toggle menu item availability
     */
    @PostMapping("/{id}/toggle-availability")
    public ResponseEntity<MenuItem> toggleMenuItemAvailability(@PathVariable String id) {
        try {
            MenuItem menuItem = menuService.toggleMenuItemAvailability(id);
            return ResponseEntity.ok(menuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Search menu items by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItems(@RequestParam String name) {
        List<MenuItem> menuItems = menuService.searchMenuItemsByName(name);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Search menu items by restaurant and name
     */
    @GetMapping("/search/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByRestaurant(
            @PathVariable String restaurantId, 
            @RequestParam String name) {
        
        List<MenuItem> menuItems = menuService.searchMenuItemsByRestaurantAndName(restaurantId, name);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> menuItems = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by restaurant and category
     */
    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurantAndCategory(
            @PathVariable String restaurantId, 
            @PathVariable String category) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurantAndCategory(restaurantId, category);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItem>> getMenuItemsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by restaurant and price range
     */
    @GetMapping("/restaurant/{restaurantId}/price-range")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurantAndPriceRange(
            @PathVariable String restaurantId,
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurantAndPriceRange(restaurantId, minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get vegetarian menu items
     */
    @GetMapping("/vegetarian")
    public ResponseEntity<List<MenuItem>> getVegetarianMenuItems() {
        List<MenuItem> menuItems = menuService.getVegetarianMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get vegan menu items
     */
    @GetMapping("/vegan")
    public ResponseEntity<List<MenuItem>> getVeganMenuItems() {
        List<MenuItem> menuItems = menuService.getVeganMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get spicy menu items
     */
    @GetMapping("/spicy")
    public ResponseEntity<List<MenuItem>> getSpicyMenuItems() {
        List<MenuItem> menuItems = menuService.getSpicyMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by spice level
     */
    @GetMapping("/spice-level/{spiceLevel}")
    public ResponseEntity<List<MenuItem>> getMenuItemsBySpiceLevel(@PathVariable int spiceLevel) {
        List<MenuItem> menuItems = menuService.getMenuItemsBySpiceLevel(spiceLevel);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get popular menu items
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MenuItem>> getPopularMenuItems() {
        List<MenuItem> menuItems = menuService.getPopularMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by tags
     */
    @GetMapping("/tags")
    public ResponseEntity<List<MenuItem>> getMenuItemsByTags(@RequestParam List<String> tags) {
        List<MenuItem> menuItems = menuService.getMenuItemsByTags(tags);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by restaurant and tags
     */
    @GetMapping("/restaurant/{restaurantId}/tags")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurantAndTags(
            @PathVariable String restaurantId, 
            @RequestParam List<String> tags) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurantAndTags(restaurantId, tags);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by preparation time
     */
    @GetMapping("/preparation-time/{maxPreparationTime}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByPreparationTime(@PathVariable int maxPreparationTime) {
        List<MenuItem> menuItems = menuService.getMenuItemsByPreparationTime(maxPreparationTime);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by calories range
     */
    @GetMapping("/calories-range")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCaloriesRange(
            @RequestParam int minCalories, 
            @RequestParam int maxCalories) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByCaloriesRange(minCalories, maxCalories);
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu items by multiple criteria
     */
    @GetMapping("/filter")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCriteria(
            @RequestParam(required = false) String restaurantId,
            @RequestParam(required = false) List<String> mealTypes,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Boolean isVegetarian,
            @RequestParam(required = false) Boolean isVegan,
            @RequestParam(required = false) Boolean isSpicy,
            @RequestParam(required = false) Integer spiceLevel) {
        
        List<MenuItem> menuItems = menuService.getMenuItemsByCriteria(
                restaurantId, mealTypes, minPrice, maxPrice, category, tags, 
                isVegetarian, isVegan, isSpicy, spiceLevel);
        
        return ResponseEntity.ok(menuItems);
    }
    
    /**
     * Get menu item statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<MenuService.MenuItemStats> getMenuItemStats(@PathVariable String id) {
        MenuService.MenuItemStats stats = menuService.getMenuItemStats(id);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }
}
