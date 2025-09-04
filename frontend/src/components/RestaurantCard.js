import React from 'react';
import { Link } from 'react-router-dom';
import { FaStar, FaClock, FaMapMarkerAlt, FaUtensils } from 'react-icons/fa';
import TimeWindow from './TimeWindow';
import './RestaurantCard.css';

const RestaurantCard = ({ restaurant }) => {
  const renderStars = (rating) => {
    const stars = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;

    for (let i = 0; i < fullStars; i++) {
      stars.push(<FaStar key={i} className="star filled" />);
    }

    if (hasHalfStar) {
      stars.push(<FaStar key="half" className="star half" />);
    }

    const emptyStars = 5 - Math.ceil(rating);
    for (let i = 0; i < emptyStars; i++) {
      stars.push(<FaStar key={`empty-${i}`} className="star empty" />);
    }

    return stars;
  };

  const formatRating = (rating) => {
    return rating.toFixed(1);
  };

  const getMealTypeAvailability = () => {
    const availableMeals = [];
    
    if (restaurant.breakfastWindow) {
      availableMeals.push('Breakfast');
    }
    if (restaurant.lunchWindow) {
      availableMeals.push('Lunch');
    }
    if (restaurant.dinnerWindow) {
      availableMeals.push('Dinner');
    }
    
    return availableMeals;
  };

  const availableMeals = getMealTypeAvailability();

  return (
    <div className="restaurant-card">
      <Link to={`/restaurants/${restaurant.id}`} className="restaurant-link">
        <div className="restaurant-image-container">
          {restaurant.imageUrl ? (
            <img 
              src={restaurant.imageUrl} 
              alt={restaurant.name}
              className="restaurant-image"
            />
          ) : (
            <div className="restaurant-image-placeholder">
              <FaUtensils className="placeholder-icon" />
            </div>
          )}
          
          <div className="restaurant-overlay">
            <div className="restaurant-rating">
              <div className="rating-stars">
                {renderStars(restaurant.rating)}
              </div>
              <span className="rating-text">
                {formatRating(restaurant.rating)} ({restaurant.reviewCount} reviews)
              </span>
            </div>
          </div>
        </div>

        <div className="restaurant-info">
          <h3 className="restaurant-name">{restaurant.name}</h3>
          
          <p className="restaurant-description">
            {restaurant.description || 'Delicious food delivered fresh to your doorstep'}
          </p>

          <div className="restaurant-details">
            <div className="restaurant-location">
              <FaMapMarkerAlt className="location-icon" />
              <span>{restaurant.address}</span>
            </div>

            {restaurant.phoneNumber && (
              <div className="restaurant-phone">
                <span>{restaurant.phoneNumber}</span>
              </div>
            )}
          </div>

          <div className="restaurant-categories">
            {restaurant.categories && restaurant.categories.slice(0, 3).map((category, index) => (
              <span key={index} className="category-tag">
                {category}
              </span>
            ))}
          </div>

          <div className="available-meals">
            <FaClock className="meals-icon" />
            <span className="meals-text">
              Available for: {availableMeals.join(', ')}
            </span>
          </div>

          <div className="time-windows">
            {restaurant.breakfastWindow && (
              <div className="meal-time-window">
                <span className="meal-label">Breakfast:</span>
                <TimeWindow mealType="BREAKFAST" />
              </div>
            )}
            
            {restaurant.lunchWindow && (
              <div className="meal-time-window">
                <span className="meal-label">Lunch:</span>
                <TimeWindow mealType="LUNCH" />
              </div>
            )}
            
            {restaurant.dinnerWindow && (
              <div className="meal-time-window">
                <span className="meal-label">Dinner:</span>
                <TimeWindow mealType="DINNER" />
              </div>
            )}
          </div>
        </div>
      </Link>
    </div>
  );
};

export default RestaurantCard;
