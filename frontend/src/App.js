import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { OrderProvider } from './contexts/OrderContext';
import { ThemeProvider } from './contexts/ThemeContext';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Restaurants from './pages/Restaurants';
import Menu from './pages/Menu';
import Cart from './pages/Cart';
import Orders from './pages/Orders';
import Profile from './pages/Profile';
import Login from './pages/Login';
import Register from './pages/Register';
import RestaurantDetail from './pages/RestaurantDetail';
import OrderTracking from './pages/OrderTracking';
import './App.css';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <OrderProvider>
          <Router>
            <div className="App">
              <Navbar />
              <main className="main-content">
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/restaurants" element={<Restaurants />} />
                  <Route path="/restaurants/:id" element={<RestaurantDetail />} />
                  <Route path="/menu/:restaurantId" element={<Menu />} />
                  <Route path="/cart" element={<Cart />} />
                  <Route path="/orders" element={<Orders />} />
                  <Route path="/orders/:id" element={<OrderTracking />} />
                  <Route path="/profile" element={<Profile />} />
                  <Route path="/login" element={<Login />} />
                  <Route path="/register" element={<Register />} />
                </Routes>
              </main>
            </div>
          </Router>
        </OrderProvider>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
