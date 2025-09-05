import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaClock, FaUtensils, FaTruck, FaShoppingCart, FaChevronDown } from 'react-icons/fa';
import { useOrder } from '../contexts/OrderContext';
import { useAuth } from '../contexts/AuthContext';
import LoginPopup from '../components/LoginPopup';
import './Home.css';

const Home = () => {
  const [selectedCategory, setSelectedCategory] = useState('BREAKFAST');
  const [showCartNotification, setShowCartNotification] = useState(false);
  const [notificationMessage, setNotificationMessage] = useState('');
  const [isFreeDelivery, setIsFreeDelivery] = useState(false);
  const [itemQuantities, setItemQuantities] = useState({});
  const [countdownTimers, setCountdownTimers] = useState({});
  const [showLoginPopup, setShowLoginPopup] = useState(false);
  const [remainingTime, setRemainingTime] = useState(null);
  const { addToCart, removeFromCart, updateCartItemQuantity, getCartItemCount, getCartTotal, cart } = useOrder();
  const { user } = useAuth();
  const navigate = useNavigate();
  
  // Free delivery threshold
  const FREE_DELIVERY_THRESHOLD = 200;

  // Function to handle start ordering
  const handleStartOrdering = () => {
    navigate('/menu/foodie_restaurant');
  };

  // Function to handle time window click and scroll to menu
  const handleTimeWindowClick = (mealType) => {
    setSelectedCategory(mealType);
    // Scroll to the menu section
    setTimeout(() => {
      const menuSection = document.querySelector('.food-categories-section');
      if (menuSection) {
        menuSection.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    }, 100);
  };

  // Function to check if a specific meal slot is currently open
  const isMealSlotOpen = (mealType) => {
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
        
      case 'CAKE':
        // Order from 7 AM (7:00) to 5 PM (17:00)
        const cakeStart = 7 * 60; // 7 AM in minutes
        const cakeEnd = 17 * 60; // 5 PM in minutes
        return currentTime >= cakeStart && currentTime <= cakeEnd;
        
      default:
        return false;
    }
  };

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
        
      case 'CAKE':
        // Order from 7 AM (7:00) to 5 PM (17:00)
        const cakeStart = 7 * 60; // 7 AM in minutes
        const cakeEnd = 17 * 60; // 5 PM in minutes
        
        if (currentTime >= cakeStart && currentTime <= cakeEnd) {
          // Ordering is currently allowed - calculate remaining time
          const minutesUntilEnd = cakeEnd - currentTime;
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
        
      case 'CAKE':
        // Order from 7 AM (7:00) to 5 PM (17:00)
        const cakeStart = 7 * 60; // 7 AM in minutes
        const cakeEnd = 17 * 60; // 5 PM in minutes
        
        if (currentTime >= cakeStart && currentTime <= cakeEnd) {
          return null; // Ordering is currently allowed
        } else if (currentTime < cakeStart) {
          // Before 7 AM - next ordering starts at 7 AM
          const minutesUntilStart = cakeStart - currentTime;
          return {
            hours: Math.floor(minutesUntilStart / 60),
            minutes: minutesUntilStart % 60,
            seconds: 0
          };
        } else if (currentTime > cakeEnd) {
          // After 5 PM - next ordering starts tomorrow at 7 AM
          const minutesUntilTomorrow = (24 * 60) - currentTime + cakeStart;
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
      restaurantId: 'foodie_restaurant',
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
      restaurantId: 'foodie_restaurant',
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
      
      case 'CAKE':
        // Order from 7 AM (7:00) to 5 PM (17:00)
        const cakeStart = 7 * 60; // 7 AM in minutes
        const cakeEnd = 17 * 60; // 5 PM in minutes
        return currentTime >= cakeStart && currentTime <= cakeEnd;
      
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
    },
    {
      type: 'CAKE',
      title: 'Cake',
      description: 'Order from 7 AM to 5 PM (Advanced Order)',
      deliveryTime: 'Next Day Delivery',
      icon: '🎂',
      color: '#ec4899'
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
    ],
    CAKE: [
      { name: 'Fruiti Cake', price: 200 },
      { name: 'Chocolate Truffle Cake', price: 500 },
      { name: 'Black Forest Cake', price: 400 },
      { name: 'Pineapple Cake', price: 350 },
      { name: 'Red Velvet Cake', price: 600 },
      { name: 'Butterscotch Cake', price: 450 },
      { name: 'Vanilla Cake', price: 300 },
      { name: 'Strawberry Cake', price: 350 },
      { name: 'Mango Mousse Cake', price: 500 },
      { name: 'Coffee Walnut Cake', price: 550 },
      { name: 'Choco Lava Cake (per piece)', price: 120 },
      { name: 'Cheesecake (Blueberry)', price: 700 },
      { name: 'KitKat Cake', price: 550 },
      { name: 'Oreo Cake', price: 500 },
      { name: 'Tiramisu Cake', price: 750 },
      { name: 'Rainbow Cake', price: 600 },
      { name: 'Rasmalai Cake', price: 650 },
      { name: 'Ferrero Rocher Cake', price: 800 }
    ]
  };

  return (
    <div className="home">
      {/* Cart Notification */}
      {showCartNotification && (
        <div className={`cart-notification ${isFreeDelivery ? 'free-delivery' : ''}`}>
          <FaShoppingCart className="cart-icon" />
          <div className="notification-content">
            <div className="notification-main">{notificationMessage}</div>
            <div className="notification-sub">Cart: {getCartItemCount()} items (Max 4 per item)</div>
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

      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1 className="hero-title">
              <span className="animated-word">Welcome</span>
              <span className="animated-word">to</span>
              <span className="animated-word">Foodie</span>
              <span className="animated-word">Restaurant</span>
            </h1>
            <p className="hero-description">
              Experience time-based food delivery with specific ordering windows for breakfast, lunch, and dinner. 
              We cook when you order because we believe in fresh food.
            </p>
            <div className="hero-actions">
              <Link to="/orders" className="btn btn-primary btn-lg">
                <FaClock className="btn-icon" />
                View Orders
              </Link>
              {!user && (
                <>
                  <Link to="/register" className="btn btn-primary btn-lg">
                    <FaUtensils className="btn-icon" />
                    Create Account
                  </Link>
                  <button 
                    className="btn btn-outline btn-lg"
                    onClick={() => setShowLoginPopup(true)}
                  >
                    <FaUtensils className="btn-icon" />
                    Log in to start ordering
                  </button>
                </>
              )}
              {user && (
                <button 
                  className="btn btn-primary btn-lg start-ordering-btn"
                  onClick={handleStartOrdering}
                >
                  <FaUtensils className="btn-icon" />
                  Start Ordering
                </button>
              )}
            </div>
          </div>
        </div>
      </section>

      {/* Time Windows Section */}
      <section className="time-windows-section">
        <div className="container">
          <h2 className="section-title">Ordering Time Windows</h2>
          <p className="section-description">
            Each meal type has specific ordering and delivery times
          </p>
          
          <div className="meal-types">
            {mealTypes.map((meal) => (
              <div 
                key={meal.type} 
                className="meal-type-card"
                onClick={() => handleTimeWindowClick(meal.type)}
                style={{ cursor: 'pointer' }}
              >
                <div className="meal-type-icon" style={{ backgroundColor: meal.color }}>
                  {meal.icon}
                </div>
                <h3 className="meal-type-title">{meal.title}</h3>
                <p className="meal-type-description">{meal.description}</p>
                <div className="delivery-time">
                  <FaTruck className="delivery-icon" />
                  <span>Delivered: {meal.deliveryTime}</span>
                </div>
                <div className="click-hint">
                  <span>Click to view menu →</span>
                </div>
                
                {/* Arrow indicator when slot is open */}
                {isMealSlotOpen(meal.type) && (
                  <div className="slot-open-indicator">
                    <FaChevronDown className="arrow-icon" />
                    <span className="arrow-text">Order Now!</span>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose Foodie Restaurant?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">
                <FaClock />
              </div>
              <h3 className="feature-title">Time-Based Ordering</h3>
              <p className="feature-description">
                Order within specific time windows to ensure fresh food delivery at the right time.
              </p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <FaTruck />
              </div>
              <h3 className="feature-title">Reliable Delivery</h3>
              <p className="feature-description">
                Get your food delivered within the promised time window, every time.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Food Categories Section */}
      <section className="food-categories-section">
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

          <div className="menu-section">
            <div className="menu-header">
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
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2 className="cta-title">Ready to Order?</h2>
            <p className="cta-description">
              Join thousands of satisfied customers who trust Foodie Restaurant for their meal delivery needs.
            </p>
            <div className="cta-actions">
              {user ? (
                <button 
                  className="btn btn-primary btn-lg start-ordering-btn"
                  onClick={handleStartOrdering}
                >
                  <FaUtensils className="btn-icon" />
                  Start Ordering
                </button>
              ) : (
                <>
                  <Link to="/register" className="btn btn-primary btn-lg">
                    <FaUtensils className="btn-icon" />
                    Create Account
                  </Link>
                  <button 
                    className="btn btn-outline btn-lg"
                    onClick={() => setShowLoginPopup(true)}
                  >
                    <FaUtensils className="btn-icon" />
                    Log in to start ordering
                  </button>
                </>
              )}
            </div>
          </div>
        </div>
      </section>

      {/* Login Popup */}
      <LoginPopup 
        isOpen={showLoginPopup} 
        onClose={() => setShowLoginPopup(false)} 
      />
    </div>
  );
};

export default Home;
