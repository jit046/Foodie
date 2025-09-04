#!/usr/bin/env python3
"""
MCP Server for Todo Application Monitoring
Connects to your Spring Boot Todo app and provides real-time monitoring capabilities
"""

import asyncio
import json
import requests
from datetime import datetime
from typing import Dict, Any, List, Optional
from mcp.server import Server
from mcp.types import Tool, TextContent
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class TodoAppMonitor:
    """
    MCP Server that monitors your deployed Spring Boot Todo application
    Provides real-time memory, GC, and health monitoring
    """
    
    def __init__(self, app_host: str = "localhost", app_port: int = 9090):
        self.app_host = app_host
        self.app_port = app_port
        self.base_url = f"http://{app_host}:{app_port}"
        self.monitoring_url = f"{self.base_url}/api/monitoring"
        
    async def _make_request(self, endpoint: str) -> Dict[str, Any]:
        """Make HTTP request to monitoring endpoint"""
        try:
            url = f"{self.monitoring_url}/{endpoint}"
            response = requests.get(url, timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            logger.error(f"Request failed: {e}")
            return {"error": f"Failed to connect to app: {str(e)}"}
    
    async def get_memory_stats(self) -> Dict[str, Any]:
        """Get detailed memory statistics"""
        return await self._make_request("memory")
    
    async def get_gc_stats(self) -> Dict[str, Any]:
        """Get garbage collection statistics"""
        return await self._make_request("gc")
    
    async def get_health_status(self) -> Dict[str, Any]:
        """Get application health status"""
        return await self._make_request("health")
    
    async def get_system_overview(self) -> Dict[str, Any]:
        """Get comprehensive system overview"""
        return await self._make_request("overview")

# Initialize the monitor
monitor = TodoAppMonitor()

# Create MCP Server
app = Server("todo-monitoring")

@app.list_tools()
async def list_tools() -> List[Tool]:
    """List available monitoring tools"""
    return [
        Tool(
            name="get_memory_usage",
            description="Get real-time heap and non-heap memory usage from the Todo app",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="get_gc_statistics", 
            description="Get garbage collection statistics and performance metrics",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="check_app_health",
            description="Check overall application health including memory warnings",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="get_full_monitoring_dashboard",
            description="Get comprehensive monitoring data including memory, GC, health, and JVM info",
            inputSchema={
                "type": "object", 
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="monitor_memory_trend",
            description="Monitor memory usage trend over specified duration",
            inputSchema={
                "type": "object",
                "properties": {
                    "duration_minutes": {
                        "type": "number",
                        "description": "Duration to monitor in minutes (default: 5)",
                        "default": 5
                    },
                    "interval_seconds": {
                        "type": "number", 
                        "description": "Interval between readings in seconds (default: 30)",
                        "default": 30
                    }
                },
                "required": []
            }
        ),
        Tool(
            name="configure_monitoring_target",
            description="Configure the target application host and port for monitoring",
            inputSchema={
                "type": "object",
                "properties": {
                    "host": {
                        "type": "string",
                        "description": "Application host (default: localhost)"
                    },
                    "port": {
                        "type": "number",
                        "description": "Application port (default: 9090)"
                    }
                },
                "required": []
            }
        )
    ]

@app.call_tool()
async def call_tool(name: str, arguments: Dict[str, Any]) -> List[TextContent]:
    """Handle tool calls"""
    global monitor
    
    if name == "get_memory_usage":
        data = await monitor.get_memory_stats()
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        heap = data['heap']
        non_heap = data['non_heap']
        
        result = f"""🧠 **Memory Usage Report**
        
**Heap Memory:**
- Used: {heap['used_mb']:.1f} MB
- Max: {heap['max_mb']:.1f} MB  
- Committed: {heap['committed_mb']:.1f} MB
- Usage: {heap['usage_percentage']:.1f}%
- Status: {'🟢 Normal' if heap['usage_percentage'] < 80 else '🟡 High' if heap['usage_percentage'] < 90 else '🔴 Critical'}

**Non-Heap Memory:**
- Used: {non_heap['used_mb']:.1f} MB
- Committed: {non_heap['committed_mb']:.1f} MB

*Updated: {datetime.fromtimestamp(data['timestamp']/1000).strftime('%H:%M:%S')}*"""
        
        return [TextContent(type="text", text=result)]
    
    elif name == "get_gc_statistics":
        data = await monitor.get_gc_stats()
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = "🗑️ **Garbage Collection Statistics**\n\n"
        
        for gc_name, stats in data.items():
            if gc_name != "timestamp":
                result += f"**{gc_name}:**\n"
                result += f"- Collections: {stats['collection_count']}\n"
                result += f"- Total Time: {stats['collection_time_ms']}ms\n"
                result += f"- Memory Pools: {', '.join(stats['memory_pool_names'])}\n\n"
        
        result += f"*Updated: {datetime.fromtimestamp(data['timestamp']/1000).strftime('%H:%M:%S')}*"
        return [TextContent(type="text", text=result)]
    
    elif name == "check_app_health":
        data = await monitor.get_health_status()
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        status_emoji = {
            "HEALTHY": "🟢",
            "WARNING": "🟡", 
            "CRITICAL": "🔴"
        }
        
        uptime_hours = data['uptime_ms'] / (1000 * 60 * 60)
        
        result = f"""💚 **Application Health Status**

**Overall Status:** {status_emoji.get(data['memory_status'], '❓')} {data['memory_status']}

**Key Metrics:**
- Heap Usage: {data['heap_usage_percent']:.1f}%
- Uptime: {uptime_hours:.1f} hours
- Active Threads: {data['thread_count']}

*Updated: {datetime.fromtimestamp(data['timestamp']/1000).strftime('%H:%M:%S')}*"""
        
        return [TextContent(type="text", text=result)]
    
    elif name == "get_full_monitoring_dashboard":
        data = await monitor.get_system_overview()
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        heap = data['memory']['heap']
        health = data['health']
        jvm = data['jvm_info']
        
        result = f"""📊 **Complete Monitoring Dashboard**

**🧠 Memory Status:** {'🟢' if health['memory_status'] == 'HEALTHY' else '🟡' if health['memory_status'] == 'WARNING' else '🔴'} {health['memory_status']}
- Heap: {heap['used_mb']:.1f}/{heap['max_mb']:.1f} MB ({heap['usage_percentage']:.1f}%)

**⚡ Performance:**
- Uptime: {health['uptime_ms'] / (1000 * 60 * 60):.1f} hours
- Threads: {health['thread_count']}

**☕ JVM Info:**
- Version: {jvm['java_version']}
- Name: {jvm['jvm_name']}
- Started: {datetime.fromtimestamp(jvm['start_time']/1000).strftime('%Y-%m-%d %H:%M:%S')}

**🗑️ Recent GC Activity:**"""
        
        for gc_name, stats in data['gc'].items():
            if gc_name != "timestamp":
                result += f"\n- {gc_name}: {stats['collection_count']} collections ({stats['collection_time_ms']}ms total)"
        
        result += f"\n\n*Updated: {datetime.fromtimestamp(data['timestamp']/1000).strftime('%H:%M:%S')}*"
        return [TextContent(type="text", text=result)]
    
    elif name == "monitor_memory_trend":
        duration = arguments.get("duration_minutes", 5)
        interval = arguments.get("interval_seconds", 30)
        
        readings = []
        result = f"📈 **Memory Trend Monitoring** (Duration: {duration} minutes, Interval: {interval}s)\n\n"
        
        for i in range(int(duration * 60 / interval)):
            data = await monitor.get_memory_stats()
            if "error" not in data:
                heap = data['heap']
                readings.append({
                    "time": i * interval,
                    "usage_mb": heap['used_mb'],
                    "percentage": heap['usage_percentage']
                })
                
                result += f"T+{i*interval:3d}s: {heap['used_mb']:6.1f} MB ({heap['usage_percentage']:5.1f}%)\n"
            
            if i < int(duration * 60 / interval) - 1:  # Don't sleep on last iteration
                await asyncio.sleep(interval)
        
        if len(readings) >= 2:
            trend = "↗️ Increasing" if readings[-1]["usage_mb"] > readings[0]["usage_mb"] else "↘️ Decreasing" if readings[-1]["usage_mb"] < readings[0]["usage_mb"] else "➡️ Stable"
            result += f"\n**Trend:** {trend}"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "configure_monitoring_target":
        host = arguments.get("host", "localhost")
        port = arguments.get("port", 9090)
        
        # Update global monitor instance
        monitor = TodoAppMonitor(host, port)
        
        # Test connection
        test_data = await monitor.get_health_status()
        if "error" in test_data:
            return [TextContent(type="text", text=f"❌ Failed to connect to {host}:{port}\n{test_data['error']}")]
        
        return [TextContent(type="text", text=f"✅ Successfully configured monitoring target: {host}:{port}")]
    
    else:
        return [TextContent(type="text", text=f"❌ Unknown tool: {name}")]

if __name__ == "__main__":
    # Run the MCP server
    import asyncio
    import sys
    from mcp.server.stdio import stdio_server
    from mcp.server.models import InitializationOptions
    
    async def main():
        logger.info("Starting Todo Monitoring MCP Server...")
        
        # Check if we're in test mode (no stdin)
        if sys.stdin.isatty():
            logger.info("Running in test mode - MCP server is ready for stdio communication")
            logger.info("To use this server with an MCP client, configure it in your mcp_client_config.json")
            logger.info("Server will wait for MCP protocol messages on stdin...")
            
            # Keep the server running for testing
            try:
                async with stdio_server() as (read_stream, write_stream):
                    init_options = InitializationOptions(
                        server_name="todo-monitoring",
                        server_version="1.0.0",
                        capabilities={}
                    )
                    await app.run(read_stream, write_stream, init_options)
            except KeyboardInterrupt:
                logger.info("Server stopped by user")
        else:
            # Normal stdio mode
            async with stdio_server() as (read_stream, write_stream):
                init_options = InitializationOptions(
                    server_name="todo-monitoring",
                    server_version="1.0.0",
                    capabilities={}
                )
                await app.run(read_stream, write_stream, init_options)
    
    asyncio.run(main())
