# Quick Setup Guide - FoodieTime Food Delivery App

## 🚀 Quick Start (Windows)

1. **Open Command Prompt or PowerShell in the project directory**

2. **Start the application:**
   ```cmd
   start_application.bat
   ```
   
   This will automatically start both the backend and frontend servers.

3. **Access the application:**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:9090
   - **API Documentation**: http://localhost:9090/swagger-ui.html

## 🚀 Quick Start (Linux/Mac)

1. **Open Terminal in the project directory**

2. **Make the script executable and run:**
   ```bash
   chmod +x start_application.sh
   ./start_application.sh
   ```

3. **Access the application:**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:9090
   - **API Documentation**: http://localhost:9090/swagger-ui.html

## 📋 Manual Setup

### Backend Setup
```bash
# In the project root directory
mvn spring-boot:run
```

### Frontend Setup
```bash
# In a new terminal, navigate to frontend directory
cd frontend
npm install
npm start
```

## 🎯 Key Features Implemented

### ✅ Time-Based Ordering System
- **Breakfast**: Order 9 PM - 7:30 AM → Deliver 10 AM - 11 AM
- **Lunch**: Order 6 AM - 10 AM → Deliver 1:15 PM - 2:00 PM
- **Dinner**: Order 12 PM - 5 PM → Deliver 7:30 PM - 8:15 PM

### ✅ Complete Backend (Spring Boot)
- User authentication and registration
- Restaurant management with time windows
- Menu item management with customizations
- Order processing and tracking
- Time-based ordering validation
- RESTful APIs with Swagger documentation

### ✅ Modern Frontend (React)
- Responsive design for mobile and desktop
- User registration and login
- Restaurant browsing and search
- Shopping cart functionality
- Order placement and tracking
- Real-time time window status
- Beautiful UI with modern design

### ✅ Database Schema
- MongoDB with embedded database for development
- Complete data models for users, restaurants, menu items, and orders
- Proper relationships and indexing

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.3, MongoDB, Maven
- **Frontend**: React 18, React Router, Axios, Styled Components
- **Database**: MongoDB (embedded)
- **Build Tools**: Maven, npm

## 📱 How to Use

1. **Register/Login**: Create an account or sign in
2. **Browse Restaurants**: View available restaurants
3. **Check Time Windows**: See which meals are available for ordering
4. **Add to Cart**: Select menu items with customizations
5. **Place Order**: Complete checkout with delivery details
6. **Track Order**: Monitor order status

## 🔧 Development Notes

- The application uses embedded MongoDB for development
- All time windows are enforced on the backend
- Frontend shows real-time ordering availability
- Responsive design works on all devices
- Complete error handling and validation

## 📚 API Documentation

Once the backend is running, visit http://localhost:9090/swagger-ui.html to see all available API endpoints and test them.

## 🎉 You're Ready!

The application is now running with all the time-based ordering features you requested. Users can only order during specific time windows, and the system enforces delivery times accordingly.

Enjoy your new food delivery application! 🍕🍔🍜
