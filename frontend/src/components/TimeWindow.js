import React, { useState, useEffect } from 'react';
import { FaClock, FaCheckCircle, FaTimesCircle } from 'react-icons/fa';
import axios from 'axios';
import './TimeWindow.css';

const TimeWindow = ({ mealType }) => {
  const [isOrderingAllowed, setIsOrderingAllowed] = useState(false);
  const [timeRemaining, setTimeRemaining] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkOrderingStatus();
    const interval = setInterval(checkOrderingStatus, 60000); // Check every minute
    return () => clearInterval(interval);
  }, [mealType]);

  const checkOrderingStatus = async () => {
    try {
      const [orderingResponse, timeResponse] = await Promise.all([
        axios.get(`/api/restaurants/ordering-allowed/${mealType}`),
        axios.get(`/api/restaurants/time-remaining/${mealType}`)
      ]);
      
      setIsOrderingAllowed(orderingResponse.data);
      setTimeRemaining(timeResponse.data);
    } catch (error) {
      console.error('Error checking ordering status:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatTimeRemaining = (timeRemaining) => {
    if (!timeRemaining || !timeRemaining.isAvailable) {
      return 'Not available';
    }

    const { hours, minutes } = timeRemaining;
    if (hours > 0) {
      return `${hours}h ${minutes}m remaining`;
    } else if (minutes > 0) {
      return `${minutes}m remaining`;
    } else {
      return 'Ordering closing soon';
    }
  };

  if (loading) {
    return (
      <div className="time-window loading">
        <div className="loading-spinner"></div>
        <span>Checking availability...</span>
      </div>
    );
  }

  return (
    <div className={`time-window ${isOrderingAllowed ? 'open' : 'closed'}`}>
      <div className="time-window-header">
        <FaClock className="time-icon" />
        <span className="time-window-status">
          {isOrderingAllowed ? 'Ordering Open' : 'Ordering Closed'}
        </span>
        {isOrderingAllowed ? (
          <FaCheckCircle className="status-icon open" />
        ) : (
          <FaTimesCircle className="status-icon closed" />
        )}
      </div>
      
      {isOrderingAllowed && timeRemaining && (
        <div className="time-remaining">
          {formatTimeRemaining(timeRemaining)}
        </div>
      )}
      
      {!isOrderingAllowed && (
        <div className="next-window-info">
          Check back during ordering hours
        </div>
      )}
    </div>
  );
};

export default TimeWindow;
