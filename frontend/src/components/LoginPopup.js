import React from 'react';
import { Link } from 'react-router-dom';
import { FaTimes, FaUser, FaLock } from 'react-icons/fa';
import './LoginPopup.css';

const LoginPopup = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="login-popup-overlay">
      <div className="login-popup">
        <button className="close-btn" onClick={onClose}>
          <FaTimes />
        </button>
        
        <div className="popup-content">
          <div className="popup-icon">
            <FaUser />
          </div>
          
          <h2>Login Required</h2>
          <p>Please login to add items to your cart and place orders.</p>
          
          <div className="popup-actions">
            <Link to="/login" className="btn btn-primary" onClick={onClose}>
              <FaLock className="btn-icon" />
              Login
            </Link>
            <Link to="/register" className="btn btn-outline" onClick={onClose}>
              Create Account
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPopup;
