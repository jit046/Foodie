#!/usr/bin/env python3
"""
Simple MCP Client Example
Shows how to communicate with MCP servers using the stdio protocol
"""

import asyncio
import json
import subprocess
import sys
from typing import Dict, Any

class SimpleMCPClient:
    """Simple MCP client that communicates with MCP servers"""
    
    def __init__(self, server_script: str):
        self.server_script = server_script
        self.process = None
    
    async def start_server(self):
        """Start the MCP server process"""
        print(f"🚀 Starting MCP server: {self.server_script}")
        self.process = subprocess.Popen(
            [sys.executable, self.server_script],
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print("✅ MCP server started")
    
    async def send_request(self, method: str, params: Dict[str, Any] = None) -> Dict[str, Any]:
        """Send a request to the MCP server"""
        if not self.process:
            raise RuntimeError("Server not started")
        
        # Create MCP request
        request = {
            "jsonrpc": "2.0",
            "id": 1,
            "method": method,
            "params": params or {}
        }
        
        # Send request
        request_json = json.dumps(request) + "\n"
        self.process.stdin.write(request_json)
        self.process.stdin.flush()
        
        # Read response
        response_line = self.process.stdout.readline()
        if response_line:
            return json.loads(response_line.strip())
        else:
            return {"error": "No response from server"}
    
    async def list_tools(self) -> Dict[str, Any]:
        """List available tools from the server"""
        return await self.send_request("tools/list")
    
    async def call_tool(self, tool_name: str, arguments: Dict[str, Any] = None) -> Dict[str, Any]:
        """Call a specific tool"""
        return await self.send_request("tools/call", {
            "name": tool_name,
            "arguments": arguments or {}
        })
    
    def stop_server(self):
        """Stop the MCP server process"""
        if self.process:
            self.process.terminate()
            self.process.wait()
            print("🛑 MCP server stopped")

async def demo_todo_monitoring_client():
    """Demo using the todo monitoring MCP server"""
    print("🔍 Demo: Todo Monitoring MCP Client")
    print("-" * 50)
    
    client = SimpleMCPClient("mcp_server/todo_monitoring_server.py")
    
    try:
        # Start the server
        await client.start_server()
        
        # Wait a moment for server to initialize
        await asyncio.sleep(2)
        
        # List available tools
        print("📋 Listing available tools...")
        tools_response = await client.list_tools()
        print(f"   Response: {tools_response}")
        
        # Call a tool (this might not work perfectly due to stdio timing)
        print("\n🛠️ Calling get_memory_usage tool...")
        memory_response = await client.call_tool("get_memory_usage")
        print(f"   Response: {memory_response}")
        
    except Exception as e:
        print(f"❌ Error: {e}")
    finally:
        client.stop_server()

async def demo_web_search_client():
    """Demo using the web search MCP server"""
    print("\n🔍 Demo: Web Search MCP Client")
    print("-" * 50)
    
    client = SimpleMCPClient("mcp_server/web_search_server.py")
    
    try:
        # Start the server
        await client.start_server()
        
        # Wait a moment for server to initialize
        await asyncio.sleep(2)
        
        # List available tools
        print("📋 Listing available tools...")
        tools_response = await client.list_tools()
        print(f"   Response: {tools_response}")
        
        # Call a tool
        print("\n🛠️ Calling search_web tool...")
        search_response = await client.call_tool("search_web", {
            "query": "MCP protocol example",
            "num_results": 2
        })
        print(f"   Response: {search_response}")
        
    except Exception as e:
        print(f"❌ Error: {e}")
    finally:
        client.stop_server()

async def main():
    """Run MCP client demos"""
    print("🚀 MCP Client Usage Demo")
    print("=" * 60)
    print("This demo shows how to communicate with MCP servers")
    print("using the stdio protocol (standard input/output)")
    print()
    
    # Demo todo monitoring
    await demo_todo_monitoring_client()
    
    # Demo web search
    await demo_web_search_client()
    
    print("\n🎉 MCP Client Demo Complete!")
    print("\n💡 Key Points:")
    print("   - MCP servers communicate via stdio (stdin/stdout)")
    print("   - Requests are sent as JSON-RPC messages")
    print("   - Real MCP clients handle the protocol more robustly")
    print("   - Your mcp_client_config.json is already configured!")

if __name__ == "__main__":
    asyncio.run(main())
