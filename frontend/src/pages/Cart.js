import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useOrder } from '../contexts/OrderContext';
import { useAuth } from '../contexts/AuthContext';
import { FaPlus, FaMinus, FaTrash, FaShoppingBag, FaArrowLeft } from 'react-icons/fa';
import { toast } from 'react-toastify';
import './Cart.css';

const Cart = () => {
  const { cart, updateCartItemQuantity, removeFromCart, getCartTotal, createOrder, loading } = useOrder();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [isCheckingOut, setIsCheckingOut] = useState(false);

  const handleQuantityChange = (itemId, newQuantity) => {
    updateCartItemQuantity(itemId, newQuantity);
  };

  const handleRemoveItem = (itemId) => {
    removeFromCart(itemId);
    toast.success('Item removed from cart');
  };

  const handleCheckout = async () => {
    if (!user) {
      toast.error('Please login to continue');
      navigate('/login');
      return;
    }

    if (cart.length === 0) {
      toast.error('Your cart is empty');
      return;
    }

    setIsCheckingOut(true);
    try {
      // For demo purposes, we'll create a simple order
      const orderData = {
        userId: user.id,
        restaurantId: cart[0].restaurantId, // Assuming all items are from the same restaurant
        mealType: 'LUNCH', // Default meal type, should be selected by user
        paymentMethod: 'CASH',
        deliveryAddress: {
          street: '123 Main Street',
          city: 'Your City',
          state: 'Your State',
          pincode: '12345',
          contactNumber: user.phoneNumber || '1234567890',
          contactName: user.firstName || user.username
        }
      };

      const result = await createOrder(orderData);
      
      if (result.success) {
        toast.success('Order placed successfully!');
        navigate(`/orders/${result.order.id}`);
      } else {
        toast.error(result.error || 'Failed to place order');
      }
    } catch (error) {
      toast.error('An error occurred while placing the order');
      console.error('Checkout error:', error);
    } finally {
      setIsCheckingOut(false);
    }
  };

  const subtotal = getCartTotal();
  const deliveryFee = 30; // Fixed delivery fee
  const tax = subtotal * 0.05; // 5% tax
  const total = subtotal + deliveryFee + tax;

  if (cart.length === 0) {
    return (
      <div className="cart-page">
        <div className="container">
          <div className="empty-cart">
            <FaShoppingBag className="empty-cart-icon" />
            <h2>Your cart is empty</h2>
            <p>Add some delicious items to get started!</p>
            <button 
              className="btn btn-primary"
              onClick={() => navigate('/restaurants')}
            >
              Browse Restaurants
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="container">
        <div className="cart-header">
          <button 
            className="back-button"
            onClick={() => navigate(-1)}
          >
            <FaArrowLeft className="back-icon" />
            Back
          </button>
          <h1 className="cart-title">Your Cart</h1>
        </div>

        <div className="cart-content">
          <div className="cart-items">
            {cart.map((item) => (
              <div key={item.id} className="cart-item">
                <div className="item-info">
                  <h3 className="item-name">{item.name}</h3>
                  <p className="item-restaurant">{item.restaurantName}</p>
                  <p className="item-price">₹{item.price.toFixed(2)} each</p>
                  
                  {item.customizations && item.customizations.length > 0 && (
                    <div className="item-customizations">
                      {item.customizations.map((customization, index) => (
                        <span key={index} className="customization-tag">
                          {customization.optionName}: {customization.selectedChoice}
                          {customization.additionalPrice > 0 && (
                            <span className="customization-price">
                              (+₹{customization.additionalPrice.toFixed(2)})
                            </span>
                          )}
                        </span>
                      ))}
                    </div>
                  )}
                </div>

                <div className="item-actions">
                  <div className="quantity-controls">
                    <button
                      className="quantity-btn"
                      onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                      disabled={item.quantity <= 1}
                    >
                      <FaMinus />
                    </button>
                    <span className="quantity-display">{item.quantity}</span>
                    <button
                      className="quantity-btn"
                      onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                    >
                      <FaPlus />
                    </button>
                  </div>

                  <div className="item-total">
                    ₹{((item.price + (item.customizations?.reduce((sum, c) => sum + (c.additionalPrice || 0), 0) || 0)) * item.quantity).toFixed(2)}
                  </div>

                  <button
                    className="remove-btn"
                    onClick={() => handleRemoveItem(item.id)}
                    title="Remove item"
                  >
                    <FaTrash />
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <h3 className="summary-title">Order Summary</h3>
            
            <div className="summary-row">
              <span>Subtotal</span>
              <span>₹{subtotal.toFixed(2)}</span>
            </div>
            
            <div className="summary-row">
              <span>Delivery Fee</span>
              <span>₹{deliveryFee.toFixed(2)}</span>
            </div>
            
            <div className="summary-row">
              <span>Tax (5%)</span>
              <span>₹{tax.toFixed(2)}</span>
            </div>
            
            <div className="summary-row total">
              <span>Total</span>
              <span>₹{total.toFixed(2)}</span>
            </div>

            <button
              className="checkout-btn"
              onClick={handleCheckout}
              disabled={loading || isCheckingOut}
            >
              {loading || isCheckingOut ? (
                <>
                  <div className="loading-spinner small"></div>
                  {isCheckingOut ? 'Placing Order...' : 'Loading...'}
                </>
              ) : (
                'Proceed to Checkout'
              )}
            </button>

            <p className="checkout-note">
              You will be redirected to complete your order details
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
