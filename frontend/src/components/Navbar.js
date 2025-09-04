import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useOrder } from '../contexts/OrderContext';
import { FaShoppingCart, FaUser, FaBars, FaTimes, FaHome, FaUtensils, FaHistory } from 'react-icons/fa';
import './Navbar.css';

const Navbar = () => {
  const { user, logout } = useAuth();
  const { getCartItemCount } = useOrder();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
    setIsMenuOpen(false);
  };

  const cartItemCount = getCartItemCount();

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo" onClick={() => setIsMenuOpen(false)}>
          <FaUtensils className="logo-icon" />
          Foodie Restaurant
        </Link>

        <div className="navbar-menu">
          <Link to="/" className="navbar-link" onClick={() => setIsMenuOpen(false)}>
            <FaHome className="link-icon" />
            Home
          </Link>
          {user && (
            <Link to="/orders" className="navbar-link" onClick={() => setIsMenuOpen(false)}>
              <FaHistory className="link-icon" />
              Orders
            </Link>
          )}
        </div>

        <div className="navbar-actions">
          {user ? (
            <>
              <Link to="/cart" className="cart-link" onClick={() => setIsMenuOpen(false)}>
                <FaShoppingCart className="cart-icon" />
                {cartItemCount > 0 && (
                  <span className="cart-badge">{cartItemCount}</span>
                )}
                Cart
              </Link>
              
              <div className="user-menu">
                <button className="user-button">
                  <FaUser className="user-icon" />
                  {user.firstName || user.username}
                </button>
                <div className="user-dropdown">
                  <Link to="/profile" className="dropdown-link">Profile</Link>
                  <button onClick={handleLogout} className="dropdown-link logout">
                    Logout
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn btn-outline btn-sm">
                Login
              </Link>
              <Link to="/register" className="btn btn-primary btn-sm">
                Register
              </Link>
            </div>
          )}

          <button 
            className="mobile-menu-toggle"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
          >
            {isMenuOpen ? <FaTimes /> : <FaBars />}
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {isMenuOpen && (
        <div className="mobile-menu">
          <Link to="/" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
            <FaHome className="link-icon" />
            Home
          </Link>
          {user && (
            <>
              <Link to="/orders" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
                <FaHistory className="link-icon" />
                Orders
              </Link>
              <Link to="/cart" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
                <FaShoppingCart className="link-icon" />
                Cart {cartItemCount > 0 && `(${cartItemCount})`}
              </Link>
              <Link to="/profile" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
                <FaUser className="link-icon" />
                Profile
              </Link>
              <button onClick={handleLogout} className="mobile-link logout">
                Logout
              </button>
            </>
          )}
          {!user && (
            <>
              <Link to="/login" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
                Login
              </Link>
              <Link to="/register" className="mobile-link" onClick={() => setIsMenuOpen(false)}>
                Register
              </Link>
            </>
          )}
        </div>
      )}
    </nav>
  );
};

export default Navbar;
