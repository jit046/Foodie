@echo off
echo Starting FoodieTime - Time-Based Food Delivery Application
echo.

echo Starting Spring Boot Backend...
start "Backend Server" cmd /k "mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 10 /nobreak > nul

echo Starting React Frontend...
cd frontend
start "Frontend Server" cmd /k "npm start"

echo.
echo Application is starting up...
echo Backend: http://localhost:9090
echo Frontend: http://localhost:3000
echo Swagger UI: http://localhost:9090/swagger-ui.html
echo.
echo Press any key to exit...
pause > nul
