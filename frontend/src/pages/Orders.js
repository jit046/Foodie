import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useOrder } from '../contexts/OrderContext';
import { FaClock, FaCheckCircle, FaTimesCircle, FaTruck, FaUtensils } from 'react-icons/fa';
import './Orders.css';

const Orders = () => {
  const { orderHistory, loading } = useOrder();
  const [selectedOrder, setSelectedOrder] = useState(null);
  const navigate = useNavigate();

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PENDING':
        return <FaClock className="status-icon pending" />;
      case 'CONFIRMED':
        return <FaCheckCircle className="status-icon confirmed" />;
      case 'PREPARING':
        return <FaUtensils className="status-icon preparing" />;
      case 'OUT_FOR_DELIVERY':
        return <FaTruck className="status-icon delivery" />;
      case 'DELIVERED':
        return <FaCheckCircle className="status-icon delivered" />;
      case 'CANCELLED':
        return <FaTimesCircle className="status-icon cancelled" />;
      default:
        return <FaClock className="status-icon pending" />;
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'PENDING':
        return 'Order Pending';
      case 'CONFIRMED':
        return 'Order Confirmed';
      case 'PREPARING':
        return 'Preparing Food';
      case 'OUT_FOR_DELIVERY':
        return 'Out for Delivery';
      case 'DELIVERED':
        return 'Delivered';
      case 'CANCELLED':
        return 'Cancelled';
      default:
        return 'Unknown Status';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="orders-page">
        <div className="container">
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading your orders...</p>
          </div>
        </div>
      </div>
    );
  }

  if (orderHistory.length === 0) {
    return (
      <div className="orders-page">
        <div className="container">
          <div className="empty-orders">
            <FaUtensils className="empty-icon" />
            <h2>No Orders Yet</h2>
            <p>You haven't placed any orders yet. Start ordering from our delicious menu!</p>
            <button 
              className="btn btn-primary"
              onClick={() => navigate('/')}
            >
              Browse Menu
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="orders-page">
      <div className="container">
        <div className="orders-header">
          <h1 className="orders-title">Your Orders</h1>
          <p className="orders-subtitle">Track your order history and status</p>
        </div>

        <div className="orders-list">
          {orderHistory.map((order) => (
            <div key={order.id} className="order-card">
              <div className="order-header">
                <div className="order-info">
                  <h3 className="order-id">Order #{order.id}</h3>
                  <p className="order-date">{formatDate(order.createdAt)}</p>
                </div>
                <div className="order-status">
                  {getStatusIcon(order.status)}
                  <span className="status-text">{getStatusText(order.status)}</span>
                </div>
              </div>

              <div className="order-details">
                <div className="order-items">
                  <h4>Items Ordered:</h4>
                  {order.items && order.items.map((item, index) => (
                    <div key={index} className="order-item">
                      <span className="item-name">{item.menuItemName}</span>
                      <span className="item-quantity">x{item.quantity}</span>
                      <span className="item-price">₹{item.unitPrice * item.quantity}</span>
                    </div>
                  ))}
                </div>

                <div className="order-summary">
                  <div className="summary-row">
                    <span>Subtotal:</span>
                    <span>₹{order.subtotal?.toFixed(2) || '0.00'}</span>
                  </div>
                  <div className="summary-row">
                    <span>Delivery Fee:</span>
                    <span>{order.deliveryFee === 0 ? 'FREE' : `₹${order.deliveryFee?.toFixed(2) || '0.00'}`}</span>
                  </div>
                  <div className="summary-row">
                    <span>Tax:</span>
                    <span>₹{order.tax?.toFixed(2) || '0.00'}</span>
                  </div>
                  <div className="summary-row total">
                    <span>Total:</span>
                    <span>₹{order.total?.toFixed(2) || '0.00'}</span>
                  </div>
                </div>
              </div>

              {order.deliveryAddress && (
                <div className="delivery-address">
                  <h4>Delivery Address:</h4>
                  <p>
                    {order.deliveryAddress.street}, {order.deliveryAddress.city}, {order.deliveryAddress.state} - {order.deliveryAddress.pincode}
                  </p>
                  <p>Contact: {order.deliveryAddress.contactName} - {order.deliveryAddress.contactNumber}</p>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Orders;
