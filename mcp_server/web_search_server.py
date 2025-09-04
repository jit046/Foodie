#!/usr/bin/env python3
"""
Web Search MCP Server
Provides real-time web search capabilities through MCP protocol
"""

import asyncio
import json
import logging
import requests
from typing import Dict, List, Any
from mcp.server import Server
# from mcp.server.models import InitializationOptions  # Not needed for basic setup
from mcp.types import TextContent, Tool

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize MCP server
app = Server("web-search")

class WebSearchService:
    """Service for performing web searches"""
    
    def __init__(self):
        self.search_engines = {
            "google": "https://www.google.com/search",
            "bing": "https://www.bing.com/search",
            "duckduckgo": "https://duckduckgo.com/html"
        }
        self.default_engine = "duckduckgo"  # Privacy-focused default
    
    def search_web(self, query: str, engine: str = None, num_results: int = 5) -> Dict[str, Any]:
        """Perform a web search and return results"""
        try:
            engine = engine or self.default_engine
            search_url = self.search_engines.get(engine, self.search_engines[self.default_engine])
            
            # For demonstration, we'll simulate search results
            # In a real implementation, you'd use proper search APIs
            results = self._simulate_search_results(query, num_results)
            
            return {
                "query": query,
                "engine": engine,
                "num_results": len(results),
                "results": results,
                "status": "success"
            }
            
        except Exception as e:
            logger.error(f"Search error: {e}")
            return {
                "query": query,
                "engine": engine,
                "error": str(e),
                "status": "error"
            }
    
    def _simulate_search_results(self, query: str, num_results: int) -> List[Dict[str, str]]:
        """Simulate search results (replace with real search API)"""
        # This is a simulation - in production, use real search APIs
        results = []
        for i in range(min(num_results, 5)):
            results.append({
                "title": f"Search Result {i+1} for '{query}'",
                "url": f"https://example{i+1}.com/{query.replace(' ', '-')}",
                "snippet": f"This is a simulated search result for the query '{query}'. Result number {i+1} provides relevant information about the topic.",
                "rank": i + 1
            })
        return results
    
    def get_search_engines(self) -> List[str]:
        """Get list of available search engines"""
        return list(self.search_engines.keys())

# Initialize search service
search_service = WebSearchService()

# Register tools
@app.list_tools()
async def list_tools() -> List[Tool]:
    """List available web search tools"""
    return [
        Tool(
            name="search_web",
            description="Search the web for information on any topic",
            inputSchema={
                "type": "object",
                "properties": {
                    "query": {
                        "type": "string",
                        "description": "The search query to look up"
                    },
                    "engine": {
                        "type": "string",
                        "description": "Search engine to use (google, bing, duckduckgo)",
                        "default": "duckduckgo"
                    },
                    "num_results": {
                        "type": "integer",
                        "description": "Number of results to return (1-10)",
                        "default": 5,
                        "minimum": 1,
                        "maximum": 10
                    }
                },
                "required": ["query"]
            }
        ),
        Tool(
            name="get_search_engines",
            description="Get list of available search engines",
            inputSchema={
                "type": "object",
                "properties": {}
            }
        ),
        Tool(
            name="search_news",
            description="Search for recent news articles",
            inputSchema={
                "type": "object",
                "properties": {
                    "query": {
                        "type": "string",
                        "description": "News search query"
                    },
                    "num_results": {
                        "type": "integer",
                        "description": "Number of news articles to return",
                        "default": 5
                    }
                },
                "required": ["query"]
            }
        ),
        Tool(
            name="search_technical",
            description="Search for technical documentation and code examples",
            inputSchema={
                "type": "object",
                "properties": {
                    "query": {
                        "type": "string",
                        "description": "Technical search query (e.g., 'Spring Boot MongoDB integration')"
                    },
                    "language": {
                        "type": "string",
                        "description": "Programming language to focus on",
                        "default": "any"
                    }
                },
                "required": ["query"]
            }
        )
    ]

@app.call_tool()
async def call_tool(name: str, arguments: Dict[str, Any]) -> List[TextContent]:
    """Handle tool calls"""
    try:
        if name == "search_web":
            query = arguments.get("query", "")
            engine = arguments.get("engine", "duckduckgo")
            num_results = arguments.get("num_results", 5)
            
            results = search_service.search_web(query, engine, num_results)
            
            if results["status"] == "success":
                response = f"🔍 **Web Search Results for: '{query}'**\n\n"
                response += f"**Search Engine:** {results['engine']}\n"
                response += f"**Results Found:** {results['num_results']}\n\n"
                
                for i, result in enumerate(results["results"], 1):
                    response += f"**{i}. {result['title']}**\n"
                    response += f"   URL: {result['url']}\n"
                    response += f"   {result['snippet']}\n\n"
                
                return [TextContent(type="text", text=response)]
            else:
                return [TextContent(type="text", text=f"❌ Search failed: {results.get('error', 'Unknown error')}")]
        
        elif name == "get_search_engines":
            engines = search_service.get_search_engines()
            response = "🔍 **Available Search Engines:**\n\n"
            for engine in engines:
                response += f"• **{engine}** - {search_service.search_engines[engine]}\n"
            return [TextContent(type="text", text=response)]
        
        elif name == "search_news":
            query = arguments.get("query", "")
            num_results = arguments.get("num_results", 5)
            
            # Simulate news search
            news_results = search_service.search_web(f"news {query}", "duckduckgo", num_results)
            
            if news_results["status"] == "success":
                response = f"📰 **News Search Results for: '{query}'**\n\n"
                for i, result in enumerate(news_results["results"], 1):
                    response += f"**{i}. {result['title']}**\n"
                    response += f"   {result['snippet']}\n"
                    response += f"   Source: {result['url']}\n\n"
                
                return [TextContent(type="text", text=response)]
            else:
                return [TextContent(type="text", text=f"❌ News search failed: {news_results.get('error', 'Unknown error')}")]
        
        elif name == "search_technical":
            query = arguments.get("query", "")
            language = arguments.get("language", "any")
            
            # Simulate technical search
            tech_query = f"{language} {query}" if language != "any" else query
            tech_results = search_service.search_web(tech_query, "duckduckgo", 5)
            
            if tech_results["status"] == "success":
                response = f"💻 **Technical Search Results for: '{query}'**\n\n"
                if language != "any":
                    response += f"**Language Focus:** {language}\n\n"
                
                for i, result in enumerate(tech_results["results"], 1):
                    response += f"**{i}. {result['title']}**\n"
                    response += f"   {result['snippet']}\n"
                    response += f"   Documentation: {result['url']}\n\n"
                
                return [TextContent(type="text", text=response)]
            else:
                return [TextContent(type="text", text=f"❌ Technical search failed: {tech_results.get('error', 'Unknown error')}")]
        
        else:
            return [TextContent(type="text", text=f"❌ Unknown tool: {name}")]
    
    except Exception as e:
        logger.error(f"Tool call error: {e}")
        return [TextContent(type="text", text=f"❌ Error executing tool: {str(e)}")]

if __name__ == "__main__":
    # Run the MCP server
    import asyncio
    import sys
    from mcp.server.stdio import stdio_server
    from mcp.server.models import InitializationOptions
    
    async def main():
        logger.info("Starting Web Search MCP Server...")
        
        # Check if we're in test mode (no stdin)
        if sys.stdin.isatty():
            logger.info("Running in test mode - MCP server is ready for stdio communication")
            logger.info("To use this server with an MCP client, configure it in your mcp_client_config.json")
            logger.info("Server will wait for MCP protocol messages on stdin...")
            
            # Keep the server running for testing
            try:
                async with stdio_server() as (read_stream, write_stream):
                    init_options = InitializationOptions(
                        server_name="web-search",
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
                    server_name="web-search",
                    server_version="1.0.0",
                    capabilities={}
                )
                await app.run(read_stream, write_stream, init_options)
    
    asyncio.run(main())
