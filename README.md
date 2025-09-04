# FoodieTime - Time-Based Food Delivery Application

A complete food delivery application similar to Swiggy with time-based ordering windows for breakfast, lunch, and dinner. Built with Spring Boot backend and React frontend.

## Features

### Time-Based Ordering System
- **Breakfast**: Order from 9:00 PM to 7:30 AM, delivered 10:00 AM - 11:00 AM
- **Lunch**: Order from 6:00 AM to 10:00 AM, delivered 1:15 PM - 2:00 PM  
- **Dinner**: Order from 12:00 PM to 5:00 PM, delivered 7:30 PM - 8:15 PM

### Core Features
- User registration and authentication
- Restaurant browsing and search
- Menu management with categories and customizations
- Shopping cart functionality
- Order placement and tracking
- Time window validation
- Real-time ordering status
- Responsive design for mobile and desktop

### Technology Stack
- **Backend**: Spring Boot 3.2.3, MongoDB, Maven
- **Frontend**: React 18, React Router, Axios, Styled Components
- **Database**: MongoDB (embedded for development)
- **Build Tools**: Maven, npm

## Project Structure

```
food-delivery-app/
├── src/main/java/com/example/todo/
│   ├── config/                 # Configuration classes
│   ├── controller/             # REST API controllers
│   ├── model/                  # Data models (User, Restaurant, MenuItem, Order)
│   ├── repository/             # MongoDB repositories
│   ├── service/                # Business logic services
│   └── TodoApplication.java    # Main application class
├── src/main/resources/
│   └── application.properties  # Application configuration
├── frontend/                   # React frontend application
│   ├── public/
│   ├── src/
│   │   ├── components/         # Reusable React components
│   │   ├── contexts/           # React context providers
│   │   ├── pages/              # Page components
│   │   └── App.js              # Main React component
│   └── package.json
└── README.md
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher
- Git

### Backend Setup

1. **Navigate to the project root directory:**
   ```bash
   cd food-delivery-app
   ```

2. **Build and run the Spring Boot application:**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

3. **The backend will start on port 9090:**
   - API Base URL: `http://localhost:9090`
   - Swagger UI: `http://localhost:9090/swagger-ui.html`

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm start
   ```

4. **The frontend will start on port 3000:**
   - Application URL: `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `PUT /api/auth/profile` - Update user profile

### Restaurants
- `GET /api/restaurants` - Get all restaurants
- `GET /api/restaurants/{id}` - Get restaurant by ID
- `POST /api/restaurants` - Create restaurant
- `PUT /api/restaurants/{id}` - Update restaurant
- `DELETE /api/restaurants/{id}` - Delete restaurant
- `GET /api/restaurants/search` - Search restaurants
- `GET /api/restaurants/ordering-windows` - Get available ordering windows

### Menu Items
- `GET /api/menu/restaurant/{restaurantId}` - Get menu items by restaurant
- `GET /api/menu/{id}` - Get menu item by ID
- `POST /api/menu` - Create menu item
- `PUT /api/menu/{id}` - Update menu item
- `DELETE /api/menu/{id}` - Delete menu item

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get user orders
- `PUT /api/orders/{id}/status` - Update order status
- `POST /api/orders/{id}/cancel` - Cancel order

## Database Schema

### User Collection
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "deliveryAddresses": [{
    "id": "string",
    "name": "string",
    "street": "string",
    "city": "string",
    "state": "string",
    "pincode": "string",
    "contactNumber": "string"
  }],
  "roles": ["string"],
  "enabled": "boolean"
}
```

### Restaurant Collection
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "address": "string",
  "phoneNumber": "string",
  "email": "string",
  "rating": "number",
  "categories": ["string"],
  "deliveryAreas": ["string"],
  "breakfastWindow": {
    "orderStartTime": "21:00",
    "orderEndTime": "07:30",
    "deliveryStartTime": "10:00",
    "deliveryEndTime": "11:00"
  },
  "lunchWindow": {
    "orderStartTime": "06:00",
    "orderEndTime": "10:00",
    "deliveryStartTime": "13:15",
    "deliveryEndTime": "14:00"
  },
  "dinnerWindow": {
    "orderStartTime": "12:00",
    "orderEndTime": "17:00",
    "deliveryStartTime": "19:30",
    "deliveryEndTime": "20:15"
  }
}
```

### MenuItem Collection
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "price": "number",
  "restaurantId": "string",
  "category": "string",
  "mealTypes": ["string"],
  "isVegetarian": "boolean",
  "isVegan": "boolean",
  "isSpicy": "boolean",
  "spiceLevel": "number",
  "calories": "number",
  "preparationTime": "number",
  "customizationOptions": [{
    "name": "string",
    "type": "string",
    "choices": [{
      "name": "string",
      "price": "number"
    }]
  }]
}
```

### Order Collection
```json
{
  "id": "string",
  "userId": "string",
  "restaurantId": "string",
  "orderNumber": "string",
  "status": "PENDING|CONFIRMED|PREPARING|READY_FOR_PICKUP|OUT_FOR_DELIVERY|DELIVERED|CANCELLED",
  "mealType": "BREAKFAST|LUNCH|DINNER",
  "orderTime": "datetime",
  "scheduledDeliveryTime": "time",
  "items": [{
    "menuItemId": "string",
    "menuItemName": "string",
    "quantity": "number",
    "unitPrice": "number",
    "customizations": [{
      "optionName": "string",
      "selectedChoice": "string",
      "additionalPrice": "number"
    }]
  }],
  "subtotal": "number",
  "deliveryFee": "number",
  "tax": "number",
  "totalAmount": "number",
  "deliveryAddress": {
    "street": "string",
    "city": "string",
    "state": "string",
    "pincode": "string",
    "contactNumber": "string"
  }
}
```

## Usage

### For Users
1. **Register/Login**: Create an account or sign in
2. **Browse Restaurants**: View available restaurants and their time windows
3. **Select Meal Type**: Choose breakfast, lunch, or dinner based on current time
4. **Add to Cart**: Add menu items to your cart with customizations
5. **Place Order**: Complete checkout with delivery address
6. **Track Order**: Monitor order status and delivery progress

### For Restaurant Owners
1. **Create Restaurant**: Set up restaurant profile with time windows
2. **Add Menu Items**: Create menu with categories and customizations
3. **Manage Orders**: View and update order status
4. **Set Availability**: Configure meal type availability

## Development

### Adding New Features
1. **Backend**: Add new models, repositories, services, and controllers
2. **Frontend**: Create new components and pages
3. **API Integration**: Update context providers and API calls

### Testing
- Backend: Use Spring Boot Test framework
- Frontend: Use React Testing Library
- API: Test endpoints using Postman or Swagger UI

## Deployment

### Backend Deployment
1. Build the JAR file: `mvn clean package`
2. Deploy to your preferred cloud platform (AWS, Azure, GCP)
3. Configure MongoDB connection
4. Set environment variables

### Frontend Deployment
1. Build the production bundle: `npm run build`
2. Deploy to static hosting (Netlify, Vercel, AWS S3)
3. Configure API base URL

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please create an issue in the repository.

---

**Note**: This is a demo application. For production use, ensure proper security measures, error handling, and testing are implemented.