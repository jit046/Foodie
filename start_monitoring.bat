@echo off
echo Starting Todo App with MCP Monitoring Integration
echo ================================================

echo.
echo Step 1: Installing MCP Server Dependencies...
cd mcp_server
pip install -r requirements.txt
cd ..

echo.
echo Step 2: Starting Spring Boot Application with JMX enabled...
start "Todo App" cmd /k "mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false\""

echo.
echo Step 3: Waiting for application to start...
timeout /t 30 /nobreak

echo.
echo Step 4: Starting MCP Monitoring Server...
start "MCP Server" cmd /k "python mcp_server/todo_monitoring_server.py"

echo.
echo ================================================
echo MCP Monitoring Setup Complete!
echo ================================================
echo.
echo Todo App is running on: http://localhost:9090
echo Swagger UI: http://localhost:9090/swagger-ui.html
echo Monitoring API: http://localhost:9090/api/monitoring/overview
echo JMX Port: 9999
echo.
echo You can now use AI assistants with MCP to monitor your application!
echo.
pause
