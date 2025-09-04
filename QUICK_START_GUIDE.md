# 🚀 Quick Start Guide - Food Delivery Application

This guide will help you quickly set up and run the Food Delivery Application on your local machine.

## 📋 Prerequisites

Before starting, ensure you have the following installed:

- **Java 17 or higher** - [Download Java](https://adoptium.net/)
- **Node.js 16 or higher** - [Download Node.js](https://nodejs.org/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Git** - [Download Git](https://git-scm.com/)

## 🏗️ Project Structure

```
food-delivery-app/
├── src/main/java/          # Spring Boot Backend
├── frontend/               # React Frontend
├── README.md              # Main documentation
├── SETUP_GUIDE.md         # Detailed setup guide
├── QUICK_START_GUIDE.md   # This file
├── pom.xml                # Maven configuration
└── start_application.bat  # Windows startup script
```

## 🚀 Quick Start (Windows)

### Option 1: Using the Startup Script (Recommended)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jit046/Foodie.git
   cd Foodie
   ```

2. **Run the startup script:**
   ```bash
   start_application.bat
   ```

   This script will automatically:
   - Start the Spring Boot backend
   - Install frontend dependencies
   - Start the React frontend

### Option 2: Manual Setup

#### Step 1: Start the Backend (Spring Boot)

1. **Open a new terminal/command prompt**
2. **Navigate to the project directory:**
   ```bash
   cd Foodie
   ```

3. **Start the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on: **http://localhost:8080**

#### Step 2: Start the Frontend (React)

1. **Open another terminal/command prompt**
2. **Navigate to the frontend directory:**
   ```bash
   cd Foodie/frontend
   ```

3. **Install dependencies:**
   ```bash
   npm install
   ```

4. **Start the React development server:**
   ```bash
   npm start
   ```

   The frontend will start on: **http://localhost:3000**

## 🚀 Quick Start (Linux/Mac)

### Option 1: Using the Startup Script

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jit046/Foodie.git
   cd Foodie
   ```

2. **Make the script executable:**
   ```bash
   chmod +x start_application.sh
   ```

3. **Run the startup script:**
   ```bash
   ./start_application.sh
   ```

### Option 2: Manual Setup

Follow the same steps as Windows, but use the commands in your terminal.

## 🌐 Accessing the Application

Once both services are running:

- **Frontend (React)**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **API Documentation (Swagger)**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

## 🔧 Troubleshooting

### Backend Issues

**Problem**: Maven compilation errors
**Solution**: 
```bash
mvn clean compile
mvn spring-boot:run
```

**Problem**: Port 8080 already in use
**Solution**: 
- Stop other applications using port 8080
- Or change the port in `src/main/resources/application.properties`

### Frontend Issues

**Problem**: `npm start` fails
**Solution**: 
```bash
cd frontend
npm install
npm start
```

**Problem**: Port 3000 already in use
**Solution**: 
- The React app will automatically ask to use a different port
- Or stop other applications using port 3000

## 📱 Using the Application

### Default Login Credentials

- **Admin User**: 
  - Username: `admin`
  - Password: `admin123`
  - Role: ADMIN

- **Regular User**: 
  - Username: `user`
  - Password: `user123`
  - Role: USER

### Key Features

1. **Time-based Ordering**:
   - Breakfast: Order 9 PM - 7:30 AM, Delivery 10 AM - 11 AM
   - Lunch: Order 6 AM - 10 AM, Delivery 1:15 PM - 2 PM
   - Dinner: Order 12 PM - 5 PM, Delivery 7:30 PM - 8:15 PM

2. **Restaurant Management**: Browse restaurants and menus
3. **Order Management**: Place and track orders
4. **User Profiles**: Manage delivery addresses and preferences

## 🔄 Pushing Changes to GitHub

### Initial Setup (First Time)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jit046/Foodie.git
   cd Foodie
   ```

2. **Verify remote origin:**
   ```bash
   git remote -v
   ```

### Making Changes and Pushing

1. **Make your changes** to the code

2. **Stage your changes:**
   ```bash
   git add .
   ```

3. **Commit your changes:**
   ```bash
   git commit -m "Your commit message describing the changes"
   ```

4. **Push to GitHub:**
   ```bash
   git push origin main
   ```

### Pulling Latest Changes

1. **Pull latest changes from GitHub:**
   ```bash
   git pull origin main
   ```

## 📚 Additional Resources

- **Detailed Setup Guide**: See `SETUP_GUIDE.md`
- **API Documentation**: Visit http://localhost:8080/swagger-ui.html
- **Main README**: See `README.md`

## 🆘 Getting Help

If you encounter any issues:

1. Check the troubleshooting section above
2. Review the detailed setup guide (`SETUP_GUIDE.md`)
3. Check the application logs in the terminal
4. Ensure all prerequisites are installed correctly

## 🎯 Next Steps

After successfully running the application:

1. Explore the time-based ordering system
2. Test the restaurant and menu management
3. Try placing orders for different meal times
4. Customize the application for your needs
5. Deploy to a cloud platform (AWS, Heroku, etc.)

---

**Happy Coding! 🍕🍔🍜**
