package com.example.todo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "menu_items")
public class MenuItem {
    @Id
    private String id;
    
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isSpicy;
    private int spiceLevel; // 1-5 scale
    
    @DocumentReference
    private String restaurantId;
    
    private String category; // e.g., "Main Course", "Appetizer", "Dessert"
    private List<String> mealTypes; // e.g., ["Breakfast", "Lunch", "Dinner"]
    private List<String> tags; // e.g., ["Popular", "New", "Chef's Special"]
    
    // Nutritional information
    private int calories;
    private int preparationTime; // in minutes
    private int servingSize; // number of people
    
    // Customization options
    private List<CustomizationOption> customizationOptions;
    
    public MenuItem() {}
    
    public MenuItem(String name, String description, BigDecimal price, String restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
        this.isAvailable = true;
        this.isVegetarian = false;
        this.isVegan = false;
        this.isSpicy = false;
        this.spiceLevel = 0;
        this.calories = 0;
        this.preparationTime = 30;
        this.servingSize = 1;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public boolean isVegetarian() { return isVegetarian; }
    public void setVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }
    
    public boolean isVegan() { return isVegan; }
    public void setVegan(boolean vegan) { isVegan = vegan; }
    
    public boolean isSpicy() { return isSpicy; }
    public void setSpicy(boolean spicy) { isSpicy = spicy; }
    
    public int getSpiceLevel() { return spiceLevel; }
    public void setSpiceLevel(int spiceLevel) { this.spiceLevel = spiceLevel; }
    
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public List<String> getMealTypes() { return mealTypes; }
    public void setMealTypes(List<String> mealTypes) { this.mealTypes = mealTypes; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    
    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    
    public int getServingSize() { return servingSize; }
    public void setServingSize(int servingSize) { this.servingSize = servingSize; }
    
    public List<CustomizationOption> getCustomizationOptions() { return customizationOptions; }
    public void setCustomizationOptions(List<CustomizationOption> customizationOptions) { this.customizationOptions = customizationOptions; }
    
    // Inner class for customization options
    public static class CustomizationOption {
        private String name;
        private String type; // "radio", "checkbox", "text"
        private List<CustomizationChoice> choices;
        private boolean isRequired;
        private BigDecimal additionalPrice;
        
        public CustomizationOption() {}
        
        public CustomizationOption(String name, String type, boolean isRequired) {
            this.name = name;
            this.type = type;
            this.isRequired = isRequired;
            this.additionalPrice = BigDecimal.ZERO;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public List<CustomizationChoice> getChoices() { return choices; }
        public void setChoices(List<CustomizationChoice> choices) { this.choices = choices; }
        
        public boolean isRequired() { return isRequired; }
        public void setRequired(boolean required) { isRequired = required; }
        
        public BigDecimal getAdditionalPrice() { return additionalPrice; }
        public void setAdditionalPrice(BigDecimal additionalPrice) { this.additionalPrice = additionalPrice; }
    }
    
    public static class CustomizationChoice {
        private String name;
        private BigDecimal price;
        private boolean isDefault;
        
        public CustomizationChoice() {}
        
        public CustomizationChoice(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
            this.isDefault = false;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public boolean isDefault() { return isDefault; }
        public void setDefault(boolean aDefault) { isDefault = aDefault; }
    }
}
