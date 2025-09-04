@echo off
echo ================================================
echo Starting Complete MCP Server Setup
echo ================================================

echo.
echo Step 1: Installing Dependencies...
cd mcp_server
pip install -r requirements.txt
if %errorlevel% neq 0 (
    echo ERROR: Failed to install dependencies
    pause
    exit /b 1
)
cd ..

echo.
echo Step 2: Starting Spring Boot Application...
start "Spring Boot Todo App" cmd /k "mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false\""

echo.
echo Step 3: Waiting for Spring Boot to start...
echo Please wait 30 seconds for the application to fully start...
timeout /t 30 /nobreak

echo.
echo Step 4: Testing Spring Boot connection...
curl -s http://localhost:9090/api/monitoring/health >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Spring Boot app is running successfully!
) else (
    echo ⚠️  Spring Boot app might not be ready yet, but continuing...
)

echo.
echo Step 5: Starting MCP Servers...
echo Starting Todo Monitoring Server...
start "MCP Todo Monitoring" cmd /k "cd mcp_server && python todo_monitoring_server.py"

echo Starting Web Search Server...
start "MCP Web Search" cmd /k "cd mcp_server && python web_search_server.py"

echo.
echo ================================================
echo 🎉 MCP Server Setup Complete!
echo ================================================
echo.
echo Your services are now running:
echo - Spring Boot Todo App: http://localhost:9090
echo - Swagger UI: http://localhost:9090/swagger-ui.html
echo - Todo Monitoring API: http://localhost:9090/api/monitoring/overview
echo - MCP Todo Monitoring Server: Running in separate window
echo - MCP Web Search Server: Running in separate window
echo.
echo You can now use AI assistants with MCP to:
echo - Monitor your Spring Boot application
echo - Search the web for information
echo.
echo Press any key to exit this window...
pause >nul
