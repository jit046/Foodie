import React from 'react';
import { useParams } from 'react-router-dom';

const RestaurantDetail = () => {
  const { id } = useParams();
  
  return (
    <div className="restaurant-detail-page">
      <div className="container">
        <h1>Restaurant Detail {id}</h1>
        <p>This page will show detailed information about the restaurant.</p>
      </div>
    </div>
  );
};

export default RestaurantDetail;
