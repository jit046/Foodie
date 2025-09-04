import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { FaClock, FaUtensils, FaTruck, FaStar, FaArrowRight } from 'react-icons/fa';
import './Home.css';

const Home = () => {
  const [selectedCategory, setSelectedCategory] = useState('BREAKFAST');

  // Function to check if ordering is allowed for a specific meal type
  const isOrderingAllowed = (mealType) => {
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();
    const currentTime = currentHour * 60 + currentMinute; // Convert to minutes

    switch (mealType) {
      case 'BREAKFAST':
        // Order from 9 PM (21:00) to 7:30 AM (7:30)
        const breakfastStart = 21 * 60; // 9 PM in minutes
        const breakfastEnd = 7 * 60 + 30; // 7:30 AM in minutes
        return currentTime >= breakfastStart || currentTime <= breakfastEnd;
      
      case 'LUNCH':
        // Order from 6 AM (6:00) to 10 AM (10:00)
        const lunchStart = 6 * 60; // 6 AM in minutes
        const lunchEnd = 10 * 60; // 10 AM in minutes
        return currentTime >= lunchStart && currentTime <= lunchEnd;
      
      case 'DINNER':
        // Order from 12 PM (12:00) to 5 PM (17:00)
        const dinnerStart = 12 * 60; // 12 PM in minutes
        const dinnerEnd = 17 * 60; // 5 PM in minutes
        return currentTime >= dinnerStart && currentTime <= dinnerEnd;
      
      default:
        return false;
    }
  };

  const mealTypes = [
    {
      type: 'BREAKFAST',
      title: 'Breakfast',
      description: 'Order from 9 PM to 7:30 AM',
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
      description: 'Order from 12 PM to 5 PM',
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
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1 className="hero-title">
              Welcome to Foodie Restaurant
            </h1>
            <p className="hero-description">
              Experience time-based food delivery with specific ordering windows for breakfast, lunch, and dinner. 
              Fresh food delivered exactly when you need it.
            </p>
            <div className="hero-actions">
              <Link to="/orders" className="btn btn-primary btn-lg">
                <FaClock className="btn-icon" />
                View Orders
              </Link>
              <Link to="/register" className="btn btn-primary btn-lg">
                <FaUtensils className="btn-icon" />
                Create Account
              </Link>
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
              <div key={meal.type} className="meal-type-card">
                <div className="meal-type-icon" style={{ backgroundColor: meal.color }}>
                  {meal.icon}
                </div>
                <h3 className="meal-type-title">{meal.title}</h3>
                <p className="meal-type-description">{meal.description}</p>
                <div className="delivery-time">
                  <FaTruck className="delivery-icon" />
                  <span>Delivered: {meal.deliveryTime}</span>
                </div>
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
            
            <div className="feature-card">
              <div className="feature-icon">
                <FaStar />
              </div>
              <h3 className="feature-title">Quality Restaurants</h3>
              <p className="feature-description">
                Choose from top-rated restaurants with verified quality and hygiene standards.
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
                <FaTruck className="delivery-icon" />
                <span>Delivered: {mealTypes.find(meal => meal.type === selectedCategory)?.deliveryTime}</span>
              </div>
            </div>
            
            <div className="menu-items">
              {menuItems[selectedCategory].map((item, index) => {
                const orderingAllowed = isOrderingAllowed(selectedCategory);
                return (
                  <div key={index} className="menu-item">
                    <div className="menu-item-info">
                      <h4 className="menu-item-name">{item.name}</h4>
                    </div>
                    <div className="menu-item-price">
                      <span className="price">₹{item.price}</span>
                      <button 
                        className={`add-to-cart-btn ${!orderingAllowed ? 'disabled' : ''}`}
                        disabled={!orderingAllowed}
                        title={!orderingAllowed ? `Ordering closed for ${selectedCategory.toLowerCase()}` : 'Add to cart'}
                      >
                        {orderingAllowed ? 'Add to Cart' : 'Ordering Closed'}
                      </button>
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
              <button className="btn btn-primary btn-lg" onClick={() => setSelectedCategory('BREAKFAST')}>
                Start Ordering
              </button>
              <Link to="/register" className="btn btn-primary btn-lg">
                Create Account
              </Link>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
