import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaClock, FaTruck, FaShoppingCart, FaArrowLeft } from 'react-icons/fa';
import { useOrder } from '../contexts/OrderContext';
import { useAuth } from '../contexts/AuthContext';
import LoginPopup from '../components/LoginPopup';
import './Menu.css';

const Menu = () => {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const [selectedCategory, setSelectedCategory] = useState('BREAKFAST');
  const [showCartNotification, setShowCartNotification] = useState(false);
  const [notificationMessage, setNotificationMessage] = useState('');
  const [isFreeDelivery, setIsFreeDelivery] = useState(false);
  const [countdownTimers, setCountdownTimers] = useState({});
  const [showLoginPopup, setShowLoginPopup] = useState(false);
  const [remainingTime, setRemainingTime] = useState(null);
  const { addToCart, removeFromCart, updateCartItemQuantity, getCartItemCount, getCartTotal, cart } = useOrder();
  const { user } = useAuth();
  
  // Free delivery threshold
  const FREE_DELIVERY_THRESHOLD = 200;

  // Function to calculate remaining time for open meal slots
  const calculateRemainingTime = (mealType) => {
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();
    const currentTime = currentHour * 60 + currentMinute; // Convert to minutes

    switch (mealType) {
      case 'BREAKFAST':
        // Order from 7 PM (19:00) to 6 AM (6:00)
        const breakfastStart = 19 * 60; // 7 PM in minutes
        const breakfastEnd = 6 * 60; // 6 AM in minutes
        
        if (currentTime >= breakfastStart || currentTime <= breakfastEnd) {
          // Ordering is currently allowed - calculate remaining time
          if (currentTime >= breakfastStart) {
            // After 7 PM, ordering ends at 6 AM next day
            const minutesUntilEnd = (24 * 60) - currentTime + breakfastEnd;
            return {
              hours: Math.floor(minutesUntilEnd / 60),
              minutes: minutesUntilEnd % 60,
              isOpen: true
            };
          } else {
            // Before 6 AM, ordering ends at 6 AM today
            const minutesUntilEnd = breakfastEnd - currentTime;
            return {
              hours: Math.floor(minutesUntilEnd / 60),
              minutes: minutesUntilEnd % 60,
              isOpen: true
            };
          }
        }
        break;
        
      case 'LUNCH':
        // Order from 6 AM (6:00) to 10 AM (10:00)
        const lunchStart = 6 * 60; // 6 AM in minutes
        const lunchEnd = 10 * 60; // 10 AM in minutes
        
        if (currentTime >= lunchStart && currentTime <= lunchEnd) {
          // Ordering is currently allowed - calculate remaining time
          const minutesUntilEnd = lunchEnd - currentTime;
          return {
            hours: Math.floor(minutesUntilEnd / 60),
            minutes: minutesUntilEnd % 60,
            isOpen: true
          };
        }
        break;
        
      case 'DINNER':
        // Order from 12 PM (12:00) to 6:45 PM (18:45)
        const dinnerStart = 12 * 60; // 12 PM in minutes
        const dinnerEnd = 18 * 60 + 45; // 6:45 PM in minutes
        
        if (currentTime >= dinnerStart && currentTime <= dinnerEnd) {
          // Ordering is currently allowed - calculate remaining time
          const minutesUntilEnd = dinnerEnd - currentTime;
          return {
            hours: Math.floor(minutesUntilEnd / 60),
            minutes: minutesUntilEnd % 60,
            isOpen: true
          };
        }
        break;
    }
    
    return { isOpen: false };
  };

  // Function to calculate countdown timer for each meal type
  const calculateCountdown = (mealType) => {
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();
    const currentTime = currentHour * 60 + currentMinute; // Convert to minutes

    switch (mealType) {
      case 'BREAKFAST':
        // Order from 7 PM (19:00) to 6 AM (6:00)
        const breakfastStart = 19 * 60; // 7 PM in minutes
        const breakfastEnd = 6 * 60; // 6 AM in minutes
        
        if (currentTime >= breakfastStart || currentTime <= breakfastEnd) {
          return null; // Ordering is currently allowed
        } else if (currentTime > breakfastEnd && currentTime < breakfastStart) {
          // Between 6 AM and 7 PM - next ordering starts at 7 PM
          const nextStart = breakfastStart;
          const minutesUntilStart = nextStart - currentTime;
          return {
            hours: Math.floor(minutesUntilStart / 60),
            minutes: minutesUntilStart % 60,
            seconds: 0
          };
        }
        break;
        
      case 'LUNCH':
        // Order from 6 AM (6:00) to 10 AM (10:00)
        const lunchStart = 6 * 60; // 6 AM in minutes
        const lunchEnd = 10 * 60; // 10 AM in minutes
        
        if (currentTime >= lunchStart && currentTime <= lunchEnd) {
          return null; // Ordering is currently allowed
        } else if (currentTime < lunchStart) {
          // Before 6 AM - next ordering starts at 6 AM
          const minutesUntilStart = lunchStart - currentTime;
          return {
            hours: Math.floor(minutesUntilStart / 60),
            minutes: minutesUntilStart % 60,
            seconds: 0
          };
        } else if (currentTime > lunchEnd) {
          // After 10 AM - next ordering starts tomorrow at 6 AM
          const minutesUntilTomorrow = (24 * 60) - currentTime + lunchStart;
          return {
            hours: Math.floor(minutesUntilTomorrow / 60),
            minutes: minutesUntilTomorrow % 60,
            seconds: 0
          };
        }
        break;
        
      case 'DINNER':
        // Order from 12 PM (12:00) to 6:45 PM (18:45)
        const dinnerStart = 12 * 60; // 12 PM in minutes
        const dinnerEnd = 18 * 60 + 45; // 6:45 PM in minutes
        
        if (currentTime >= dinnerStart && currentTime <= dinnerEnd) {
          return null; // Ordering is currently allowed
        } else if (currentTime < dinnerStart) {
          // Before 12 PM - next ordering starts at 12 PM
          const minutesUntilStart = dinnerStart - currentTime;
          return {
            hours: Math.floor(minutesUntilStart / 60),
            minutes: minutesUntilStart % 60,
            seconds: 0
          };
        } else if (currentTime > dinnerEnd) {
          // After 6:45 PM - next ordering starts tomorrow at 12 PM
          const minutesUntilTomorrow = (24 * 60) - currentTime + dinnerStart;
          return {
            hours: Math.floor(minutesUntilTomorrow / 60),
            minutes: minutesUntilTomorrow % 60,
            seconds: 0
          };
        }
        break;
        
      default:
        return null;
    }
    
    return null;
  };

  // Update countdown timers every second
  useEffect(() => {
    const updateTimers = () => {
      const timers = {};
      ['BREAKFAST', 'LUNCH', 'DINNER'].forEach(mealType => {
        timers[mealType] = calculateCountdown(mealType);
      });
      setCountdownTimers(timers);
    };

    // Update immediately
    updateTimers();

    // Update every second
    const interval = setInterval(updateTimers, 1000);

    return () => clearInterval(interval);
  }, []);

  // Update remaining time for current selected category
  useEffect(() => {
    const updateRemainingTime = () => {
      const timeInfo = calculateRemainingTime(selectedCategory);
      setRemainingTime(timeInfo);
    };

    // Update immediately
    updateRemainingTime();

    // Update every minute
    const interval = setInterval(updateRemainingTime, 60000);

    return () => clearInterval(interval);
  }, [selectedCategory]);

  // Function to get item quantity from cart
  const getItemQuantity = (item) => {
    const itemId = `${selectedCategory}_${item.name.replace(/\s+/g, '_').toLowerCase()}`;
    const cartItem = cart.find(cartItem => cartItem.menuItemId === itemId);
    return cartItem ? cartItem.quantity : 0;
  };

  // Function to handle adding item to cart
  const handleAddToCart = (item) => {
    // Check if user is authenticated
    if (!user) {
      setShowLoginPopup(true);
      return;
    }

    const menuItem = {
      id: `${selectedCategory}_${item.name.replace(/\s+/g, '_').toLowerCase()}`,
      name: item.name,
      price: item.price,
      restaurantId: restaurantId,
      restaurantName: 'Foodie Restaurant'
    };
    
    const result = addToCart(menuItem);
    
    if (result && !result.success) {
      // Show error notification
      setNotificationMessage(result.error);
      setIsFreeDelivery(false);
      setShowCartNotification(true);
      setTimeout(() => setShowCartNotification(false), 4000);
      return;
    }
    
    // Calculate notification message for free delivery
    setTimeout(() => {
      const currentTotal = getCartTotal();
      const remainingAmount = FREE_DELIVERY_THRESHOLD - currentTotal;
      
      if (remainingAmount > 0) {
        setNotificationMessage(`Add ₹${remainingAmount} more for free delivery!`);
        setIsFreeDelivery(false);
      } else {
        setNotificationMessage('🎉 You qualify for free delivery!');
        setIsFreeDelivery(true);
      }
      
      setShowCartNotification(true);
      setTimeout(() => setShowCartNotification(false), 4000);
    }, 100);
  };

  // Function to handle increasing quantity
  const handleIncreaseQuantity = (item) => {
    // Check if user is authenticated
    if (!user) {
      setShowLoginPopup(true);
      return;
    }

    const menuItem = {
      id: `${selectedCategory}_${item.name.replace(/\s+/g, '_').toLowerCase()}`,
      name: item.name,
      price: item.price,
      restaurantId: restaurantId,
      restaurantName: 'Foodie Restaurant'
    };
    
    const result = addToCart(menuItem);
    
    if (result && !result.success) {
      // Show error notification
      setNotificationMessage(result.error);
      setIsFreeDelivery(false);
      setShowCartNotification(true);
      setTimeout(() => setShowCartNotification(false), 4000);
      return;
    }
    
    // Calculate notification message for free delivery
    setTimeout(() => {
      const currentTotal = getCartTotal();
      const remainingAmount = FREE_DELIVERY_THRESHOLD - currentTotal;
      
      if (remainingAmount > 0) {
        setNotificationMessage(`Add ₹${remainingAmount} more for free delivery!`);
        setIsFreeDelivery(false);
      } else {
        setNotificationMessage('🎉 You qualify for free delivery!');
        setIsFreeDelivery(true);
      }
      
      setShowCartNotification(true);
      setTimeout(() => setShowCartNotification(false), 4000);
    }, 100);
  };

  // Function to handle decreasing quantity
  const handleDecreaseQuantity = (item) => {
    const itemId = `${selectedCategory}_${item.name.replace(/\s+/g, '_').toLowerCase()}`;
    const cartItem = cart.find(cartItem => cartItem.menuItemId === itemId);
    
    if (cartItem) {
      if (cartItem.quantity > 1) {
        updateCartItemQuantity(cartItem.id, cartItem.quantity - 1);
      } else {
        removeFromCart(cartItem.id);
      }
    }
  };

  // Function to check if ordering is allowed for a specific meal type
  const isOrderingAllowed = (mealType) => {
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();
    const currentTime = currentHour * 60 + currentMinute; // Convert to minutes

    switch (mealType) {
      case 'BREAKFAST':
        // Order from 7 PM (19:00) to 6 AM (6:00)
        const breakfastStart = 19 * 60; // 7 PM in minutes
        const breakfastEnd = 6 * 60; // 6 AM in minutes
        return currentTime >= breakfastStart || currentTime <= breakfastEnd;
      
      case 'LUNCH':
        // Order from 6 AM (6:00) to 10 AM (10:00)
        const lunchStart = 6 * 60; // 6 AM in minutes
        const lunchEnd = 10 * 60; // 10 AM in minutes
        return currentTime >= lunchStart && currentTime <= lunchEnd;
      
      case 'DINNER':
        // Order from 12 PM (12:00) to 6:45 PM (18:45)
        const dinnerStart = 12 * 60; // 12 PM in minutes
        const dinnerEnd = 18 * 60 + 45; // 6:45 PM in minutes
        return currentTime >= dinnerStart && currentTime <= dinnerEnd;
      
      default:
        return false;
    }
  };

  const mealTypes = [
    {
      type: 'BREAKFAST',
      title: 'Breakfast',
      description: 'Order from 7 PM to 6 AM',
      deliveryTime: '10:00 AM - 11:00 AM',
      icon: '🌅',
      color: '#fbbf24'
    },
    {
      type: 'LUNCH',
      title: 'Lunch',
      description: 'Order from 6 AM to 10 AM',
      deliveryTime: '1:15 PM - 2:00 PM',
      icon: '☀️',
      color: '#3b82f6'
    },
    {
      type: 'DINNER',
      title: 'Dinner',
      description: 'Order from 12 PM to 6:45 PM',
      deliveryTime: '7:30 PM - 8:15 PM',
      icon: '🌙',
      color: '#8b5cf6'
    }
  ];

  const menuItems = {
    BREAKFAST: [
      { name: 'Idli with Sambar (2 pcs)', price: 24 },
      { name: 'Masala Dosa', price: 35 },
      { name: 'Poha', price: 20 },
      { name: 'Aloo Paratha (with curd)', price: 30 },
      { name: 'Bread Omelette', price: 25 },
      { name: 'Tea', price: 10 },
      { name: 'Filter Coffee', price: 15 }
    ],
    LUNCH: [
      { name: 'Veg Thali', price: 60 },
      { name: 'Chicken Biryani', price: 120 },
      { name: 'Paneer Butter Masala + Roti', price: 80 },
      { name: 'Dal Tadka + Rice', price: 50 },
      { name: 'Rajma Chawal', price: 45 },
      { name: 'Curd', price: 10 },
      { name: 'Butter Roti (1 pc)', price: 8 }
    ],
    DINNER: [
      { name: 'Chapati (2 pcs) + Sabzi', price: 35 },
      { name: 'Egg Curry + Rice', price: 70 },
      { name: 'Chicken Curry + Roti (2 pcs)', price: 110 },
      { name: 'Veg Pulao', price: 40 },
      { name: 'Mix Veg + Paratha', price: 50 },
      { name: 'Gulab Jamun (2 pcs)', price: 20 },
      { name: 'Buttermilk', price: 12 }
    ]
  };
  
  return (
    <div className="menu-page">
      {/* Cart Notification */}
      {showCartNotification && (
        <div className={`cart-notification ${isFreeDelivery ? 'free-delivery' : ''}`}>
          <FaShoppingCart className="cart-icon" />
          <div className="notification-content">
            <div className="notification-main">{notificationMessage}</div>
            <div className="notification-sub">Cart: {getCartItemCount()}/4 items</div>
          </div>
          <button 
            className="notification-close"
            onClick={() => setShowCartNotification(false)}
            title="Close notification"
          >
            ×
          </button>
        </div>
      )}

      {/* Header */}
      <div className="menu-header">
        <div className="container">
          <div className="header-content">
            <button className="back-btn" onClick={() => navigate('/')}>
              <FaArrowLeft className="back-icon" />
              Back to Home
            </button>
            <h1 className="menu-page-title">Foodie Restaurant Menu</h1>
            <div className="cart-info">
              <FaShoppingCart className="cart-icon" />
              <span>{getCartItemCount()} items</span>
            </div>
          </div>
        </div>
      </div>

      {/* Category Selection */}
      <div className="category-section">
        <div className="container">
          <h2 className="section-title">Choose Your Meal</h2>
          <p className="section-description">
            Select a category to view available items and prices
          </p>
          
          <div className="category-buttons">
            {mealTypes.map((meal) => (
              <button
                key={meal.type}
                className={`category-btn ${selectedCategory === meal.type ? 'active' : ''}`}
                onClick={() => setSelectedCategory(meal.type)}
                style={{ backgroundColor: selectedCategory === meal.type ? meal.color : '#f3f4f6' }}
              >
                <span className="category-icon">{meal.icon}</span>
                <span className="category-title">{meal.title}</span>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Menu Items */}
      <div className="menu-section">
      <div className="container">
          <div className="menu-content">
            <div className="menu-header-info">
              <h3 className="menu-title">
                {mealTypes.find(meal => meal.type === selectedCategory)?.title} Menu
              </h3>
              <div className="delivery-info">
                <FaClock className="delivery-icon" />
                <span>Order: {mealTypes.find(meal => meal.type === selectedCategory)?.description}</span>
              </div>
              <div className="delivery-info">
                <FaTruck className="delivery-icon" />
                <span>Delivered: {mealTypes.find(meal => meal.type === selectedCategory)?.deliveryTime}</span>
              </div>
              
              {/* Countdown Timer */}
              {countdownTimers[selectedCategory] && (
                <div className="countdown-timer">
                  <div className="countdown-icon">
                    <FaClock />
                  </div>
                  <div className="countdown-content">
                    <div className="countdown-text">Ordering starts in:</div>
                    <div className="countdown-time">
                      {countdownTimers[selectedCategory].hours > 0 && (
                        <span className="time-unit">
                          {countdownTimers[selectedCategory].hours}h
                        </span>
                      )}
                      <span className="time-unit">
                        {countdownTimers[selectedCategory].minutes}m
                      </span>
                    </div>
                  </div>
                </div>
              )}

              {/* Order Within Time Display */}
              {remainingTime && remainingTime.isOpen && (
                <div className="order-within-timer">
                  <div className="order-within-icon">
                    <FaClock />
                  </div>
                  <div className="order-within-content">
                    <div className="order-within-text">Order within:</div>
                    <div className="order-within-time">
                      {remainingTime.hours > 0 && (
                        <span className="time-unit">
                          {remainingTime.hours}h
                        </span>
                      )}
                      <span className="time-unit">
                        {remainingTime.minutes}m
                      </span>
                    </div>
                  </div>
                </div>
              )}
            </div>
            
            <div className="menu-items">
              {menuItems[selectedCategory].map((item, index) => {
                const orderingAllowed = isOrderingAllowed(selectedCategory);
                const quantity = getItemQuantity(item);
                
                return (
                  <div key={index} className="menu-item">
                    <div className="menu-item-info">
                      <h4 className="menu-item-name">{item.name}</h4>
                    </div>
                    <div className="menu-item-price">
                      <span className="price">₹{item.price}</span>
                      {quantity === 0 ? (
                        <button 
                          className={`add-to-cart-btn ${!orderingAllowed ? 'disabled' : ''}`}
                          disabled={!orderingAllowed}
                          onClick={() => orderingAllowed && handleAddToCart(item)}
                          title={!orderingAllowed ? `Ordering closed for ${selectedCategory.toLowerCase()}` : 'Add to cart'}
                        >
                          {orderingAllowed ? 'Add to Cart' : 'Ordering Closed'}
                        </button>
                      ) : (
                        <div className="quantity-controls">
                          <button 
                            className="quantity-btn decrease"
                            onClick={() => handleDecreaseQuantity(item)}
                            title="Decrease quantity"
                          >
                            -
                          </button>
                          <span className="quantity-display">{quantity}</span>
                          <button 
                            className="quantity-btn increase"
                            onClick={() => handleIncreaseQuantity(item)}
                            title="Increase quantity"
                          >
                            +
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>

      {/* Login Popup */}
      <LoginPopup 
        isOpen={showLoginPopup} 
        onClose={() => setShowLoginPopup(false)} 
      />
    </div>
  );
};

export default Menu;
