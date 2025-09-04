import React, { useState, useEffect } from 'react';
import { useOrder } from '../contexts/OrderContext';
import { FaUser, FaEnvelope, FaPhone, FaMapMarkerAlt, FaEdit, FaSave, FaTimes, FaShoppingBag, FaClock } from 'react-icons/fa';
import { toast } from 'react-toastify';
import './Profile.css';

const Profile = () => {
  const { orderHistory, getCartItemCount } = useOrder();
  const [isEditing, setIsEditing] = useState(false);
  const [userProfile, setUserProfile] = useState({
    firstName: 'Guest',
    lastName: 'User',
    email: 'guest@foodie.com',
    phoneNumber: '1234567890',
    address: {
      street: '123 Main Street',
      city: 'Your City',
      state: 'Your State',
      pincode: '12345'
    },
    joinDate: new Date().toISOString(),
    totalOrders: 0,
    favoriteItems: []
  });

  const [editForm, setEditForm] = useState({ ...userProfile });

  useEffect(() => {
    // Load user profile from localStorage
    const savedProfile = localStorage.getItem('userProfile');
    if (savedProfile) {
      setUserProfile(JSON.parse(savedProfile));
      setEditForm(JSON.parse(savedProfile));
    }
    
    // Update total orders count
    setUserProfile(prev => ({
      ...prev,
      totalOrders: orderHistory.length
    }));
  }, [orderHistory]);

  const handleEdit = () => {
    setIsEditing(true);
    setEditForm({ ...userProfile });
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditForm({ ...userProfile });
  };

  const handleSave = () => {
    // Validate form
    if (!editForm.firstName.trim() || !editForm.email.trim()) {
      toast.error('Please fill in required fields');
      return;
    }

    if (!editForm.email.includes('@')) {
      toast.error('Please enter a valid email address');
      return;
    }

    // Update profile
    const updatedProfile = {
      ...editForm,
      updatedAt: new Date().toISOString()
    };
    
    setUserProfile(updatedProfile);
    localStorage.setItem('userProfile', JSON.stringify(updatedProfile));
    setIsEditing(false);
    toast.success('Profile updated successfully!');
  };

  const handleInputChange = (field, value) => {
    if (field.includes('.')) {
      const [parent, child] = field.split('.');
      setEditForm(prev => ({
        ...prev,
        [parent]: {
          ...prev[parent],
          [child]: value
        }
      }));
    } else {
      setEditForm(prev => ({
        ...prev,
        [field]: value
      }));
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-IN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  return (
    <div className="profile-page">
      <div className="container">
        <div className="profile-header">
          <h1 className="profile-title">Your Profile</h1>
          <p className="profile-subtitle">Manage your account information and preferences</p>
        </div>

        <div className="profile-content">
          {/* Profile Information Card */}
          <div className="profile-card">
            <div className="card-header">
              <h2 className="card-title">
                <FaUser className="card-icon" />
                Personal Information
              </h2>
              {!isEditing && (
                <button className="edit-btn" onClick={handleEdit}>
                  <FaEdit className="btn-icon" />
                  Edit Profile
                </button>
              )}
            </div>

            <div className="card-content">
              {isEditing ? (
                <div className="edit-form">
                  <div className="form-row">
                    <div className="form-group">
                      <label className="form-label">First Name *</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.firstName}
                        onChange={(e) => handleInputChange('firstName', e.target.value)}
                        placeholder="Enter first name"
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Last Name</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.lastName}
                        onChange={(e) => handleInputChange('lastName', e.target.value)}
                        placeholder="Enter last name"
                      />
                    </div>
                  </div>

                  <div className="form-group">
                    <label className="form-label">Email Address *</label>
                    <input
                      type="email"
                      className="form-input"
                      value={editForm.email}
                      onChange={(e) => handleInputChange('email', e.target.value)}
                      placeholder="Enter email address"
                    />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Phone Number</label>
                    <input
                      type="tel"
                      className="form-input"
                      value={editForm.phoneNumber}
                      onChange={(e) => handleInputChange('phoneNumber', e.target.value)}
                      placeholder="Enter phone number"
                    />
                  </div>

                  <div className="form-actions">
                    <button className="btn btn-primary" onClick={handleSave}>
                      <FaSave className="btn-icon" />
                      Save Changes
                    </button>
                    <button className="btn btn-outline" onClick={handleCancel}>
                      <FaTimes className="btn-icon" />
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <div className="profile-info">
                  <div className="info-row">
                    <span className="info-label">Name:</span>
                    <span className="info-value">{userProfile.firstName} {userProfile.lastName}</span>
                  </div>
                  <div className="info-row">
                    <span className="info-label">Email:</span>
                    <span className="info-value">{userProfile.email}</span>
                  </div>
                  <div className="info-row">
                    <span className="info-label">Phone:</span>
                    <span className="info-value">{userProfile.phoneNumber}</span>
                  </div>
                  <div className="info-row">
                    <span className="info-label">Member Since:</span>
                    <span className="info-value">{formatDate(userProfile.joinDate)}</span>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Address Information Card */}
          <div className="profile-card">
            <div className="card-header">
              <h2 className="card-title">
                <FaMapMarkerAlt className="card-icon" />
                Delivery Address
              </h2>
            </div>
            <div className="card-content">
              {isEditing ? (
                <div className="edit-form">
                  <div className="form-group">
                    <label className="form-label">Street Address</label>
                    <input
                      type="text"
                      className="form-input"
                      value={editForm.address.street}
                      onChange={(e) => handleInputChange('address.street', e.target.value)}
                      placeholder="Enter street address"
                    />
                  </div>
                  <div className="form-row">
                    <div className="form-group">
                      <label className="form-label">City</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.address.city}
                        onChange={(e) => handleInputChange('address.city', e.target.value)}
                        placeholder="Enter city"
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">State</label>
                      <input
                        type="text"
                        className="form-input"
                        value={editForm.address.state}
                        onChange={(e) => handleInputChange('address.state', e.target.value)}
                        placeholder="Enter state"
                      />
                    </div>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Pincode</label>
                    <input
                      type="text"
                      className="form-input"
                      value={editForm.address.pincode}
                      onChange={(e) => handleInputChange('address.pincode', e.target.value)}
                      placeholder="Enter pincode"
                    />
                  </div>
                </div>
              ) : (
                <div className="profile-info">
                  <div className="info-row">
                    <span className="info-label">Address:</span>
                    <span className="info-value">
                      {userProfile.address.street}, {userProfile.address.city}, {userProfile.address.state} - {userProfile.address.pincode}
                    </span>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Order Statistics Card */}
          <div className="profile-card">
            <div className="card-header">
              <h2 className="card-title">
                <FaShoppingBag className="card-icon" />
                Order Statistics
              </h2>
            </div>
            <div className="card-content">
              <div className="stats-grid">
                <div className="stat-item">
                  <div className="stat-number">{userProfile.totalOrders}</div>
                  <div className="stat-label">Total Orders</div>
                </div>
                <div className="stat-item">
                  <div className="stat-number">{getCartItemCount()}</div>
                  <div className="stat-label">Items in Cart</div>
                </div>
                <div className="stat-item">
                  <div className="stat-number">
                    {orderHistory.filter(order => order.status === 'DELIVERED').length}
                  </div>
                  <div className="stat-label">Delivered Orders</div>
                </div>
                <div className="stat-item">
                  <div className="stat-number">
                    {orderHistory.filter(order => order.deliveryFee === 0).length}
                  </div>
                  <div className="stat-label">Free Deliveries</div>
                </div>
              </div>
            </div>
          </div>

          {/* Recent Orders Card */}
          {orderHistory.length > 0 && (
            <div className="profile-card">
              <div className="card-header">
                <h2 className="card-title">
                  <FaClock className="card-icon" />
                  Recent Orders
                </h2>
              </div>
              <div className="card-content">
                <div className="recent-orders">
                  {orderHistory.slice(0, 3).map((order) => (
                    <div key={order.id} className="recent-order">
                      <div className="order-info">
                        <span className="order-id">Order #{order.id}</span>
                        <span className="order-date">{formatDate(order.createdAt)}</span>
                      </div>
                      <div className="order-status">
                        <span className={`status-badge ${order.status.toLowerCase()}`}>
                          {order.status}
                        </span>
                        <span className="order-total">₹{order.total?.toFixed(2) || '0.00'}</span>
                      </div>
                    </div>
                  ))}
                </div>
                {orderHistory.length > 3 && (
                  <button 
                    className="btn btn-outline btn-sm"
                    onClick={() => window.location.href = '/orders'}
                  >
                    View All Orders
                  </button>
                )}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;
