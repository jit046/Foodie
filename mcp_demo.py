#!/usr/bin/env python3
"""
Short demo showing how to use MCP servers directly
"""

import asyncio
import sys
import os

# Add the mcp_server directory to the path
sys.path.append('mcp_server')

async def demo_todo_monitoring():
    """Demo the todo monitoring server functions"""
    print("🔍 Demo: Todo Monitoring Server")
    print("-" * 40)
    
    # Import the monitoring service
    from todo_monitoring_server import monitor
    
    # Get memory stats
    print("📊 Getting memory statistics...")
    memory_stats = await monitor.get_memory_stats()
    heap = memory_stats['heap']
    print(f"   Heap Usage: {heap['used_mb']:.1f} MB / {heap['max_mb']:.1f} MB ({heap['usage_percentage']:.1f}%)")
    
    # Get health status
    print("💚 Getting health status...")
    health = await monitor.get_health_status()
    print(f"   Status: {health['memory_status']}")
    print(f"   Uptime: {health['uptime_ms'] / (1000 * 60):.1f} minutes")
    
    print("✅ Todo monitoring demo complete!\n")

async def demo_web_search():
    """Demo the web search server functions"""
    print("🔍 Demo: Web Search Server")
    print("-" * 40)
    
    # Import the search service
    from web_search_server import search_service
    
    # Perform a web search
    print("🌐 Performing web search...")
    results = search_service.search_web("Spring Boot MCP integration", "duckduckgo", 3)
    print(f"   Found {results['num_results']} results:")
    for i, result in enumerate(results['results'], 1):
        print(f"   {i}. {result['title']}")
    
    # Get available search engines
    print("\n🔧 Available search engines:")
    engines = search_service.get_search_engines()
    for engine in engines:
        print(f"   - {engine}")
    
    print("✅ Web search demo complete!\n")

async def demo_mcp_tools():
    """Demo the MCP tools directly"""
    print("🛠️ Demo: MCP Tools")
    print("-" * 40)
    
    # Import the MCP server apps
    from todo_monitoring_server import app as todo_app
    from web_search_server import app as web_app
    
    # List available tools from todo monitoring
    print("📋 Todo Monitoring Tools:")
    # Note: In a real MCP client, you'd call list_tools() through the MCP protocol
    print("   - get_memory_usage")
    print("   - get_gc_statistics") 
    print("   - check_app_health")
    print("   - get_full_monitoring_dashboard")
    
    # List available tools from web search
    print("\n📋 Web Search Tools:")
    print("   - search_web")
    print("   - search_news")
    print("   - search_technical")
    print("   - get_search_engines")
    
    print("✅ MCP tools demo complete!\n")

async def main():
    """Run all demos"""
    print("🚀 MCP Server Usage Demo")
    print("=" * 50)
    
    # Check if Spring Boot is running
    try:
        import requests
        response = requests.get('http://localhost:9090/api/monitoring/health', timeout=2)
        if response.status_code == 200:
            print("✅ Spring Boot app is running")
        else:
            print("⚠️  Spring Boot app might not be running")
    except:
        print("❌ Spring Boot app is not accessible")
        print("   Please start it with: mvn spring-boot:run")
        return
    
    print()
    
    # Run demos
    await demo_todo_monitoring()
    await demo_web_search()
    await demo_mcp_tools()
    
    print("🎉 Demo complete!")
    print("\n💡 To use with an MCP client:")
    print("   1. Configure mcp_client_config.json")
    print("   2. Use an MCP-compatible AI assistant")
    print("   3. The servers will communicate via stdio protocol")

if __name__ == "__main__":
    asyncio.run(main())
