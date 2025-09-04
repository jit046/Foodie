import React from 'react';
import { useParams } from 'react-router-dom';

const Menu = () => {
  const { restaurantId } = useParams();
  
  return (
    <div className="menu-page">
      <div className="container">
        <h1>Menu for Restaurant {restaurantId}</h1>
        <p>This page will show the restaurant's menu items.</p>
      </div>
    </div>
  );
};

export default Menu;
