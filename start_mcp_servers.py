#!/usr/bin/env python3
"""
Python startup script for MCP servers
Alternative to the batch file for cross-platform compatibility
"""

import asyncio
import subprocess
import sys
import time
import os
from pathlib import Path

def install_dependencies():
    """Install required dependencies"""
    print("📦 Installing dependencies...")
    try:
        subprocess.run([sys.executable, "-m", "pip", "install", "-r", "mcp_server/requirements.txt"], 
                      check=True, cwd=os.getcwd())
        print("✅ Dependencies installed successfully")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ Failed to install dependencies: {e}")
        return False

def start_spring_boot():
    """Start Spring Boot application"""
    print("🚀 Starting Spring Boot application...")
    try:
        # Start Spring Boot in background
        subprocess.Popen(["mvn", "spring-boot:run"], 
                        cwd=os.getcwd(),
                        stdout=subprocess.PIPE,
                        stderr=subprocess.PIPE)
        print("✅ Spring Boot started (running in background)")
        return True
    except Exception as e:
        print(f"❌ Failed to start Spring Boot: {e}")
        return False

def wait_for_spring_boot():
    """Wait for Spring Boot to be ready"""
    print("⏳ Waiting for Spring Boot to start...")
    import requests
    
    for i in range(30):  # Wait up to 30 seconds
        try:
            response = requests.get("http://localhost:9090/api/monitoring/health", timeout=2)
            if response.status_code == 200:
                print("✅ Spring Boot is ready!")
                return True
        except:
            pass
        time.sleep(1)
        print(f"   Waiting... ({i+1}/30)")
    
    print("⚠️  Spring Boot might not be ready yet, but continuing...")
    return False

def start_mcp_servers():
    """Start MCP servers"""
    print("🔧 Starting MCP servers...")
    
    # Start todo monitoring server
    print("   Starting Todo Monitoring Server...")
    subprocess.Popen([sys.executable, "mcp_server/todo_monitoring_server.py"], 
                    cwd=os.getcwd())
    
    # Start web search server
    print("   Starting Web Search Server...")
    subprocess.Popen([sys.executable, "mcp_server/web_search_server.py"], 
                    cwd=os.getcwd())
    
    print("✅ MCP servers started")

def main():
    """Main startup function"""
    print("🎯 MCP Server Startup Script")
    print("=" * 40)
    
    # Change to script directory
    script_dir = Path(__file__).parent
    os.chdir(script_dir)
    
    steps = [
        ("Installing Dependencies", install_dependencies),
        ("Starting Spring Boot", start_spring_boot),
        ("Waiting for Spring Boot", wait_for_spring_boot),
        ("Starting MCP Servers", start_mcp_servers),
    ]
    
    for step_name, step_func in steps:
        print(f"\n📋 {step_name}...")
        if not step_func():
            print(f"❌ {step_name} failed!")
            return False
    
    print("\n" + "=" * 40)
    print("🎉 All servers started successfully!")
    print("\nYour services are running:")
    print("- Spring Boot Todo App: http://localhost:9090")
    print("- Swagger UI: http://localhost:9090/swagger-ui.html")
    print("- MCP Todo Monitoring Server: Running")
    print("- MCP Web Search Server: Running")
    print("\nPress Ctrl+C to stop all services")
    
    try:
        # Keep script running
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\n👋 Shutting down...")
        return True

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)
