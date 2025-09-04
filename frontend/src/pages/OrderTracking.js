import React from 'react';
import { useParams } from 'react-router-dom';

const OrderTracking = () => {
  const { id } = useParams();
  
  return (
    <div className="order-tracking-page">
      <div className="container">
        <h1>Order Tracking {id}</h1>
        <p>This page will show the order tracking information.</p>
      </div>
    </div>
  );
};

export default OrderTracking;
