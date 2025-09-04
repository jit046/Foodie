package com.example.todo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    
    @DocumentReference
    private String userId;
    
    @DocumentReference
    private String restaurantId;
    
    private String orderNumber; // Human-readable order number
    private OrderStatus status;
    private MealType mealType;
    private LocalDateTime orderTime;
    private LocalTime scheduledDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    
    // Order details
    private List<OrderItem> items;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    
    // Delivery information
    private DeliveryAddress deliveryAddress;
    private String deliveryInstructions;
    private String deliveryPersonName;
    private String deliveryPersonPhone;
    
    // Payment information
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentTransactionId;
    
    // Order metadata
    private String specialInstructions;
    private int estimatedPreparationTime; // in minutes
    private int estimatedDeliveryTime; // in minutes
    private String promoCode;
    private BigDecimal discountAmount;
    
    public Order() {}
    
    public Order(String userId, String restaurantId, MealType mealType) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.mealType = mealType;
        this.status = OrderStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orderTime = LocalDateTime.now();
        this.subtotal = BigDecimal.ZERO;
        this.deliveryFee = BigDecimal.ZERO;
        this.tax = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
    }
    
    // Enums
    public enum OrderStatus {
        PENDING,           // Order placed but not confirmed
        CONFIRMED,         // Order confirmed by restaurant
        PREPARING,         // Food is being prepared
        READY_FOR_PICKUP,  // Food is ready for delivery
        OUT_FOR_DELIVERY,  // Order is out for delivery
        DELIVERED,         // Order has been delivered
        CANCELLED,         // Order was cancelled
        REFUNDED           // Order was refunded
    }
    
    public enum MealType {
        BREAKFAST, LUNCH, DINNER
    }
    
    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, UPI, WALLET, NET_BANKING
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    
    public LocalTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
    public void setScheduledDeliveryTime(LocalTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
    
    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    
    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public DeliveryAddress getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(DeliveryAddress deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    
    public String getDeliveryPersonName() { return deliveryPersonName; }
    public void setDeliveryPersonName(String deliveryPersonName) { this.deliveryPersonName = deliveryPersonName; }
    
    public String getDeliveryPersonPhone() { return deliveryPersonPhone; }
    public void setDeliveryPersonPhone(String deliveryPersonPhone) { this.deliveryPersonPhone = deliveryPersonPhone; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public void setPaymentTransactionId(String paymentTransactionId) { this.paymentTransactionId = paymentTransactionId; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public int getEstimatedPreparationTime() { return estimatedPreparationTime; }
    public void setEstimatedPreparationTime(int estimatedPreparationTime) { this.estimatedPreparationTime = estimatedPreparationTime; }
    
    public int getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(int estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }
    
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    // Inner class for order items
    public static class OrderItem {
        private String menuItemId;
        private String menuItemName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private List<CustomizationSelection> customizations;
        private String specialInstructions;
        
        public OrderItem() {}
        
        public OrderItem(String menuItemId, String menuItemName, int quantity, BigDecimal unitPrice) {
            this.menuItemId = menuItemId;
            this.menuItemName = menuItemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        
        // Getters and Setters
        public String getMenuItemId() { return menuItemId; }
        public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
        
        public String getMenuItemName() { return menuItemName; }
        public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        
        public List<CustomizationSelection> getCustomizations() { return customizations; }
        public void setCustomizations(List<CustomizationSelection> customizations) { this.customizations = customizations; }
        
        public String getSpecialInstructions() { return specialInstructions; }
        public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    }
    
    public static class CustomizationSelection {
        private String optionName;
        private String selectedChoice;
        private BigDecimal additionalPrice;
        
        public CustomizationSelection() {}
        
        public CustomizationSelection(String optionName, String selectedChoice, BigDecimal additionalPrice) {
            this.optionName = optionName;
            this.selectedChoice = selectedChoice;
            this.additionalPrice = additionalPrice;
        }
        
        // Getters and Setters
        public String getOptionName() { return optionName; }
        public void setOptionName(String optionName) { this.optionName = optionName; }
        
        public String getSelectedChoice() { return selectedChoice; }
        public void setSelectedChoice(String selectedChoice) { this.selectedChoice = selectedChoice; }
        
        public BigDecimal getAdditionalPrice() { return additionalPrice; }
        public void setAdditionalPrice(BigDecimal additionalPrice) { this.additionalPrice = additionalPrice; }
    }
    
    public static class DeliveryAddress {
        private String street;
        private String city;
        private String state;
        private String pincode;
        private String landmark;
        private String contactNumber;
        private String contactName;
        
        public DeliveryAddress() {}
        
        public DeliveryAddress(String street, String city, String state, String pincode, String contactNumber) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.pincode = pincode;
            this.contactNumber = contactNumber;
        }
        
        // Getters and Setters
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
    }
}
