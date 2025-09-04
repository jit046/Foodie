#!/usr/bin/env python3
"""
Test MCP Server Functionality
This script tests the MCP servers by directly calling their functions
"""

import asyncio
import sys
import os

# Add the mcp_server directory to the path
sys.path.append('mcp_server')

async def test_todo_monitoring_server():
    """Test the todo monitoring server functionality"""
    print("🧪 Testing Todo Monitoring MCP Server...")
    
    try:
        # Import the server components
        from todo_monitoring_server import app, monitor
        
        # Test the monitor class directly
        print("  📊 Testing memory stats...")
        memory_stats = await monitor.get_memory_stats()
        print(f"    ✅ Memory stats: {memory_stats}")
        
        print("  💚 Testing health status...")
        health_status = await monitor.get_health_status()
        print(f"    ✅ Health status: {health_status}")
        
        print("  📈 Testing system overview...")
        system_overview = await monitor.get_system_overview()
        print(f"    ✅ System overview: {system_overview}")
        
        print("  ✅ Todo Monitoring Server: All tests passed!")
        return True
        
    except Exception as e:
        print(f"  ❌ Todo Monitoring Server test failed: {e}")
        return False

async def test_web_search_server():
    """Test the web search server functionality"""
    print("🧪 Testing Web Search MCP Server...")
    
    try:
        # Import the server components
        from web_search_server import app, search_service
        
        # Test the search service directly
        print("  🔍 Testing web search...")
        search_results = search_service.search_web("test query", "duckduckgo", 3)
        print(f"    ✅ Search results: {search_results}")
        
        print("  📰 Testing news search...")
        news_results = search_service.search_web("news test", "duckduckgo", 2)
        print(f"    ✅ News results: {news_results}")
        
        print("  🔧 Testing search engines list...")
        engines = search_service.get_search_engines()
        print(f"    ✅ Available engines: {engines}")
        
        print("  ✅ Web Search Server: All tests passed!")
        return True
        
    except Exception as e:
        print(f"  ❌ Web Search Server test failed: {e}")
        return False

async def test_spring_boot_connection():
    """Test Spring Boot application connection"""
    print("🧪 Testing Spring Boot Connection...")
    
    try:
        import requests
        
        # Test health endpoint
        response = requests.get('http://localhost:9090/api/monitoring/health', timeout=5)
        if response.status_code == 200:
            health_data = response.json()
            print(f"    ✅ Health endpoint: {health_data}")
        else:
            print(f"    ❌ Health endpoint failed: {response.status_code}")
            return False
        
        # Test overview endpoint
        response = requests.get('http://localhost:9090/api/monitoring/overview', timeout=5)
        if response.status_code == 200:
            overview_data = response.json()
            print(f"    ✅ Overview endpoint: Memory usage {overview_data['memory']['heap']['usage_percentage']:.1f}%")
        else:
            print(f"    ❌ Overview endpoint failed: {response.status_code}")
            return False
        
        print("  ✅ Spring Boot Connection: All tests passed!")
        return True
        
    except Exception as e:
        print(f"  ❌ Spring Boot Connection test failed: {e}")
        return False

async def main():
    """Run all tests"""
    print("🚀 MCP Server Functionality Test")
    print("=" * 50)
    
    tests = [
        ("Spring Boot Connection", test_spring_boot_connection),
        ("Todo Monitoring Server", test_todo_monitoring_server),
        ("Web Search Server", test_web_search_server),
    ]
    
    passed = 0
    total = len(tests)
    
    for test_name, test_func in tests:
        print(f"\n📋 Running: {test_name}")
        try:
            if await test_func():
                passed += 1
            else:
                print(f"❌ {test_name} failed")
        except Exception as e:
            print(f"❌ {test_name} failed with exception: {e}")
    
    print("\n" + "=" * 50)
    print(f"📊 Test Results: {passed}/{total} tests passed")
    
    if passed == total:
        print("🎉 All tests passed! Your MCP servers are working correctly.")
        print("\n💡 Note: MCP servers are designed to work with MCP clients.")
        print("   They communicate via stdio (standard input/output) protocol.")
        print("   To use them with an AI assistant, configure them in your MCP client.")
    else:
        print("⚠️  Some tests failed. Check the errors above.")
    
    return passed == total

if __name__ == "__main__":
    success = asyncio.run(main())
    sys.exit(0 if success else 1)
