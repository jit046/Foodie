import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const OrderContext = createContext();

export const useOrder = () => {
  const context = useContext(OrderContext);
  if (!context) {
    throw new Error('useOrder must be used within an OrderProvider');
  }
  return context;
};

export const OrderProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [currentOrder, setCurrentOrder] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);
  const [loading, setLoading] = useState(false);

  // Load cart and order history from localStorage on mount
  useEffect(() => {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      setCart(JSON.parse(savedCart));
    }
    
    const savedOrderHistory = localStorage.getItem('orderHistory');
    if (savedOrderHistory) {
      setOrderHistory(JSON.parse(savedOrderHistory));
    }
  }, []);

  // Save cart to localStorage whenever it changes
  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(cart));
  }, [cart]);

  // Save order history to localStorage whenever it changes
  useEffect(() => {
    localStorage.setItem('orderHistory', JSON.stringify(orderHistory));
  }, [orderHistory]);

  const addToCart = (menuItem, quantity = 1, customizations = []) => {
    // Check if adding this item would exceed the 4-item limit
    const currentItemCount = cart.reduce((total, item) => total + item.quantity, 0);
    
    if (currentItemCount + quantity > 4) {
      return {
        success: false,
        error: `Cannot add more items. Maximum 4 items allowed in cart. You currently have ${currentItemCount} items.`
      };
    }

    const cartItem = {
      id: `${menuItem.id}_${Date.now()}`,
      menuItemId: menuItem.id,
      name: menuItem.name,
      price: menuItem.price,
      quantity,
      customizations,
      restaurantId: menuItem.restaurantId,
      restaurantName: menuItem.restaurantName || 'Unknown Restaurant'
    };

    setCart(prevCart => {
      const existingItem = prevCart.find(item => 
        item.menuItemId === cartItem.menuItemId && 
        JSON.stringify(item.customizations) === JSON.stringify(customizations)
      );

      if (existingItem) {
        // Check if updating existing item would exceed limit
        const newQuantity = existingItem.quantity + quantity;
        if (newQuantity > 4) {
          return prevCart; // Don't update if it would exceed limit
        }
        return prevCart.map(item =>
          item.id === existingItem.id
            ? { ...item, quantity: newQuantity }
            : item
        );
      } else {
        return [...prevCart, cartItem];
      }
    });

    return { success: true };
  };

  const removeFromCart = (itemId) => {
    setCart(prevCart => prevCart.filter(item => item.id !== itemId));
  };

  const updateCartItemQuantity = (itemId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(itemId);
      return;
    }

    // Check if updating this item would exceed the 4-item limit
    const currentItemCount = cart.reduce((total, item) => total + item.quantity, 0);
    const itemToUpdate = cart.find(item => item.id === itemId);
    
    if (itemToUpdate) {
      const currentItemQuantity = itemToUpdate.quantity;
      const newTotalCount = currentItemCount - currentItemQuantity + quantity;
      
      if (newTotalCount > 4) {
        return {
          success: false,
          error: `Cannot update quantity. Maximum 4 items allowed in cart.`
        };
      }
    }

    setCart(prevCart =>
      prevCart.map(item =>
        item.id === itemId ? { ...item, quantity } : item
      )
    );

    return { success: true };
  };

  const clearCart = () => {
    setCart([]);
  };

  const getCartTotal = () => {
    return cart.reduce((total, item) => {
      const itemTotal = item.price * item.quantity;
      const customizationTotal = item.customizations.reduce(
        (sum, customization) => sum + (customization.additionalPrice || 0),
        0
      );
      return total + (itemTotal + customizationTotal);
    }, 0);
  };

  const getCartItemCount = () => {
    return cart.reduce((total, item) => total + item.quantity, 0);
  };

  const createOrder = async (orderData) => {
    setLoading(true);
    try {
      const orderPayload = {
        ...orderData,
        items: cart.map(item => ({
          menuItemId: item.menuItemId,
          menuItemName: item.name,
          quantity: item.quantity,
          unitPrice: item.price,
          customizations: item.customizations
        }))
      };

      const response = await axios.post('/api/orders', orderPayload);
      const newOrder = response.data;
      
      setCurrentOrder(newOrder);
      setOrderHistory(prev => [newOrder, ...prev]);
      clearCart();
      
      return { success: true, order: newOrder };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Order creation failed' 
      };
    } finally {
      setLoading(false);
    }
  };

  const getOrderById = async (orderId) => {
    setLoading(true);
    try {
      const response = await axios.get(`/api/orders/${orderId}`);
      return { success: true, order: response.data };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Failed to fetch order' 
      };
    } finally {
      setLoading(false);
    }
  };

  const getUserOrders = async (userId) => {
    setLoading(true);
    try {
      const response = await axios.get(`/api/orders/user/${userId}`);
      setOrderHistory(response.data);
      return { success: true, orders: response.data };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Failed to fetch orders' 
      };
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (orderId, status) => {
    setLoading(true);
    try {
      const response = await axios.put(`/api/orders/${orderId}/status`, null, {
        params: { status }
      });
      
      // Update order in history if it exists
      setOrderHistory(prev =>
        prev.map(order =>
          order.id === orderId ? response.data : order
        )
      );
      
      return { success: true, order: response.data };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Failed to update order status' 
      };
    } finally {
      setLoading(false);
    }
  };

  const cancelOrder = async (orderId, reason) => {
    setLoading(true);
    try {
      const response = await axios.post(`/api/orders/${orderId}/cancel`, null, {
        params: { reason }
      });
      
      // Update order in history
      setOrderHistory(prev =>
        prev.map(order =>
          order.id === orderId ? response.data : order
        )
      );
      
      return { success: true, order: response.data };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Failed to cancel order' 
      };
    } finally {
      setLoading(false);
    }
  };

  const getOrderStats = async (userId) => {
    setLoading(true);
    try {
      const response = await axios.get(`/api/orders/user/${userId}/stats`);
      return { success: true, stats: response.data };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Failed to fetch order stats' 
      };
    } finally {
      setLoading(false);
    }
  };

  const value = {
    cart,
    currentOrder,
    orderHistory,
    setOrderHistory,
    loading,
    addToCart,
    removeFromCart,
    updateCartItemQuantity,
    clearCart,
    getCartTotal,
    getCartItemCount,
    createOrder,
    getOrderById,
    getUserOrders,
    updateOrderStatus,
    cancelOrder,
    getOrderStats
  };

  return (
    <OrderContext.Provider value={value}>
      {children}
    </OrderContext.Provider>
  );
};
