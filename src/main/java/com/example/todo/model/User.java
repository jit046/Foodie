package com.example.todo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String username;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    
    private Set<String> roles;
    private boolean enabled = true;
    
    // Food delivery specific fields
    private List<DeliveryAddress> deliveryAddresses;
    private String defaultDeliveryAddressId;
    private List<String> favoriteRestaurantIds;
    private List<String> orderHistory;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Preferences
    private boolean emailNotifications = true;
    private boolean smsNotifications = true;
    private boolean pushNotifications = true;
    private String preferredLanguage = "en";
    private String preferredCurrency = "INR";
    
    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User(String username, String email, String password, Set<String> roles) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public List<DeliveryAddress> getDeliveryAddresses() { return deliveryAddresses; }
    public void setDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
    
    public String getDefaultDeliveryAddressId() { return defaultDeliveryAddressId; }
    public void setDefaultDeliveryAddressId(String defaultDeliveryAddressId) { this.defaultDeliveryAddressId = defaultDeliveryAddressId; }
    
    public List<String> getFavoriteRestaurantIds() { return favoriteRestaurantIds; }
    public void setFavoriteRestaurantIds(List<String> favoriteRestaurantIds) { this.favoriteRestaurantIds = favoriteRestaurantIds; }
    
    public List<String> getOrderHistory() { return orderHistory; }
    public void setOrderHistory(List<String> orderHistory) { this.orderHistory = orderHistory; }
    
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    
    public boolean isSmsNotifications() { return smsNotifications; }
    public void setSmsNotifications(boolean smsNotifications) { this.smsNotifications = smsNotifications; }
    
    public boolean isPushNotifications() { return pushNotifications; }
    public void setPushNotifications(boolean pushNotifications) { this.pushNotifications = pushNotifications; }
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getPreferredCurrency() { return preferredCurrency; }
    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }
    
    // Inner class for delivery addresses
    public static class DeliveryAddress {
        private String id;
        private String name; // e.g., "Home", "Office", "Other"
        private String street;
        private String city;
        private String state;
        private String pincode;
        private String landmark;
        private String contactNumber;
        private String contactName;
        private boolean isDefault;
        private String instructions; // Special delivery instructions
        
        public DeliveryAddress() {}
        
        public DeliveryAddress(String name, String street, String city, String state, String pincode, String contactNumber) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.state = state;
            this.pincode = pincode;
            this.contactNumber = contactNumber;
            this.isDefault = false;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        
        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }
        
        public String getLandmark() { return landmark; }
        public void setLandmark(String landmark) { this.landmark = landmark; }
        
        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
        
        public String getContactName() { return contactName; }
        public void setContactName(String contactName) { this.contactName = contactName; }
        
        public boolean isDefault() { return isDefault; }
        public void setDefault(boolean aDefault) { isDefault = aDefault; }
        
        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }
}
