import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useOrder } from '../contexts/OrderContext';
import { useAuth } from '../contexts/AuthContext';
import { FaPlus, FaMinus, FaTrash, FaShoppingBag, FaArrowLeft } from 'react-icons/fa';
import { toast } from 'react-toastify';
import './Cart.css';

const Cart = () => {
  const { cart, updateCartItemQuantity, removeFromCart, getCartTotal, createOrder, loading, orderHistory, setOrderHistory, clearCart } = useOrder();
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
    if (cart.length === 0) {
      toast.error('Your cart is empty');
      return;
    }

    setIsCheckingOut(true);
    try {
      // Create order data
      const orderData = {
        userId: 'guest_user', // Since we removed auth, use guest user
        restaurantId: cart[0].restaurantId,
        restaurantName: cart[0].restaurantName,
        mealType: 'LUNCH', // Default meal type
        paymentMethod: 'CASH',
        status: 'PENDING',
        subtotal: subtotal,
        deliveryFee: deliveryFee,
        tax: tax,
        total: total,
        deliveryAddress: {
          street: '123 Main Street',
          city: 'Your City',
          state: 'Your State',
          pincode: '12345',
          contactNumber: '1234567890',
          contactName: 'Guest User'
        },
        items: cart.map(item => ({
          menuItemId: item.menuItemId,
          menuItemName: item.name,
          quantity: item.quantity,
          unitPrice: item.price,
          customizations: item.customizations || []
        }))
      };

      // Create order locally (since we don't have backend order creation)
      const newOrder = {
        id: `ORDER_${Date.now()}`,
        ...orderData,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };

      // Add to order history
      setOrderHistory(prev => [newOrder, ...prev]);
      
      // Clear cart
      clearCart();
      
      toast.success('Order placed successfully!');
      navigate('/orders');
    } catch (error) {
      toast.error('An error occurred while placing the order');
      console.error('Checkout error:', error);
    } finally {
      setIsCheckingOut(false);
    }
  };

  const subtotal = getCartTotal();
  const FREE_DELIVERY_THRESHOLD = 200;
  const deliveryFee = subtotal >= FREE_DELIVERY_THRESHOLD ? 0 : 30; // Free delivery for orders ₹200+
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
              <span className={deliveryFee === 0 ? 'free-delivery' : ''}>
                {deliveryFee === 0 ? 'FREE' : `₹${deliveryFee.toFixed(2)}`}
              </span>
            </div>
            
            {deliveryFee > 0 && (
              <div className="free-delivery-message">
                <span className="free-delivery-text">
                  Add ₹{(FREE_DELIVERY_THRESHOLD - subtotal).toFixed(0)} more for free delivery!
                </span>
              </div>
            )}
            
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
