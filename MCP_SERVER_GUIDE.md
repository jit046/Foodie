# 🚀 MCP Server Running Guide

## 📋 **Step-by-Step Instructions**

### **Step 1: Start Spring Boot Application**
```bash
# Start the Spring Boot todo application
mvn spring-boot:run
```
**Wait for:** "Started TodoApplication" message and port 9090 to be available

### **Step 2: Test Spring Boot is Running**
```bash
# Test the monitoring endpoint
python -c "import requests; r = requests.get('http://localhost:9090/api/monitoring/health'); print('Status:', r.status_code); print('Response:', r.json())"
```
**Expected:** Status 200 with health data

### **Step 3: Start MCP Servers**

#### **Option A: Start Todo Monitoring Server**
```bash
# In a new terminal window
python mcp_server/todo_monitoring_server.py
```
**Expected:** "Starting Todo Monitoring MCP Server..." message

#### **Option B: Start Web Search Server**
```bash
# In another new terminal window  
python mcp_server/web_search_server.py
```
**Expected:** "Starting Web Search MCP Server..." message

### **Step 4: Test MCP Servers**
```bash
# Run the test script
python test_mcp_servers.py
```
**Expected:** All tests should pass

## 🔧 **Troubleshooting**

### **Port 9090 Already in Use**
```bash
# Find process using port 9090
netstat -ano | findstr :9090

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### **MCP Server Import Errors**
```bash
# Install dependencies
pip install -r mcp_server/requirements.txt
```

### **Spring Boot Not Starting**
```bash
# Check if Maven is installed
mvn --version

# Clean and rebuild
mvn clean compile
mvn spring-boot:run
```

## 📊 **Verification Commands**

### **Check Spring Boot Status**
```bash
curl http://localhost:9090/api/monitoring/health
```

### **Check Port Usage**
```bash
netstat -an | findstr :9090
```

### **Check Running Processes**
```bash
tasklist | findstr java
tasklist | findstr python
```

## 🎯 **Expected Results**

When everything is running correctly, you should see:

1. **Spring Boot**: Running on http://localhost:9090
2. **Todo Monitoring MCP Server**: Running and ready for MCP connections
3. **Web Search MCP Server**: Running and ready for MCP connections
4. **Test Script**: All tests passing (3/3)

## 🔗 **Available Endpoints**

- **Main App**: http://localhost:9090
- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **Health Check**: http://localhost:9090/api/monitoring/health
- **Monitoring Overview**: http://localhost:9090/api/monitoring/overview

## 📝 **MCP Client Configuration**

Your `mcp_client_config.json` is already configured with both servers:

```json
{
  "mcpServers": {
    "todo-monitoring": {
      "command": "python",
      "args": ["mcp_server/todo_monitoring_server.py"]
    },
    "web-search": {
      "command": "python", 
      "args": ["mcp_server/web_search_server.py"]
    }
  }
}
```

## ⚠️ **Important Notes**

1. **Start Spring Boot first** - MCP servers need the Spring Boot app running
2. **Use separate terminals** - Each MCP server needs its own terminal window
3. **Wait for startup** - Give Spring Boot 30-60 seconds to fully start
4. **Check logs** - Look for error messages in the terminal output

## 🆘 **If Something Goes Wrong**

1. **Kill all processes**: `taskkill /F /IM java.exe` and `taskkill /F /IM python.exe`
2. **Restart everything** in the correct order
3. **Check the logs** for specific error messages
4. **Run the test script** to verify everything is working
