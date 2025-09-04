import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaClock, FaUtensils, FaTruck, FaStar, FaArrowRight } from 'react-icons/fa';
import axios from 'axios';
import TimeWindow from '../components/TimeWindow';
import RestaurantCard from '../components/RestaurantCard';
import LoadingSpinner from '../components/LoadingSpinner';
import './Home.css';

const Home = () => {
  const [timeWindows, setTimeWindows] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchTimeWindows();
    fetchRestaurants();
  }, []);

  const fetchTimeWindows = async () => {
    try {
      const response = await axios.get('/api/restaurants/ordering-windows');
      setTimeWindows(response.data);
    } catch (error) {
      console.error('Error fetching time windows:', error);
    }
  };

  const fetchRestaurants = async () => {
    try {
      const response = await axios.get('/api/restaurants');
      setRestaurants(response.data.slice(0, 6)); // Show only first 6 restaurants
    } catch (error) {
      setError('Failed to load restaurants');
      console.error('Error fetching restaurants:', error);
    } finally {
      setLoading(false);
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

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1 className="hero-title">
              Order Food at the Right Time
            </h1>
            <p className="hero-description">
              Experience time-based food delivery with specific ordering windows for breakfast, lunch, and dinner. 
              Fresh food delivered exactly when you need it.
            </p>
            <div className="hero-actions">
              <Link to="/restaurants" className="btn btn-primary btn-lg">
                <FaUtensils className="btn-icon" />
                Browse Restaurants
              </Link>
              <Link to="/orders" className="btn btn-outline btn-lg">
                <FaClock className="btn-icon" />
                View Orders
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
                <TimeWindow mealType={meal.type} />
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose FoodieTime?</h2>
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

      {/* Restaurants Section */}
      <section className="restaurants-section">
        <div className="container">
          <div className="section-header">
            <h2 className="section-title">Featured Restaurants</h2>
            <Link to="/restaurants" className="view-all-link">
              View All Restaurants
              <FaArrowRight className="arrow-icon" />
            </Link>
          </div>
          
          {error ? (
            <div className="error-message">
              <p>{error}</p>
              <button onClick={fetchRestaurants} className="btn btn-primary">
                Try Again
              </button>
            </div>
          ) : (
            <div className="restaurants-grid">
              {restaurants.map((restaurant) => (
                <RestaurantCard key={restaurant.id} restaurant={restaurant} />
              ))}
            </div>
          )}
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2 className="cta-title">Ready to Order?</h2>
            <p className="cta-description">
              Join thousands of satisfied customers who trust FoodieTime for their meal delivery needs.
            </p>
            <div className="cta-actions">
              <Link to="/restaurants" className="btn btn-primary btn-lg">
                Start Ordering
              </Link>
              <Link to="/register" className="btn btn-outline btn-lg">
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
