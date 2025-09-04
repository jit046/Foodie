#!/bin/bash

echo "Starting FoodieTime - Time-Based Food Delivery Application"
echo

echo "Starting Spring Boot Backend..."
mvn spring-boot:run &
BACKEND_PID=$!

echo "Waiting for backend to start..."
sleep 10

echo "Starting React Frontend..."
cd frontend
npm start &
FRONTEND_PID=$!

echo
echo "Application is starting up..."
echo "Backend: http://localhost:9090"
echo "Frontend: http://localhost:3000"
echo "Swagger UI: http://localhost:9090/swagger-ui.html"
echo
echo "Press Ctrl+C to stop both servers"

# Function to cleanup processes on exit
cleanup() {
    echo "Stopping servers..."
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    exit
}

# Set trap to cleanup on script exit
trap cleanup SIGINT SIGTERM

# Wait for user to stop
wait
