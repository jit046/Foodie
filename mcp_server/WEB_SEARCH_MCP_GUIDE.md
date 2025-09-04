# Web Search MCP Server Guide

## 🚀 **Overview**
The Web Search MCP Server provides real-time web search capabilities through the Model Context Protocol (MCP). This allows AI assistants to search the web for current information, news, technical documentation, and more.

## 📋 **Features**

### **Available Tools:**
1. **`search_web`** - General web search
2. **`get_search_engines`** - List available search engines
3. **`search_news`** - Search for recent news articles
4. **`search_technical`** - Search for technical documentation and code examples

### **Supported Search Engines:**
- **DuckDuckGo** (default) - Privacy-focused
- **Google** - Comprehensive results
- **Bing** - Microsoft's search engine

## 🛠️ **Setup Instructions**

### **Step 1: Install Dependencies**
```bash
cd mcp_server
pip install -r web_search_requirements.txt
```

### **Step 2: Start the Server**
```bash
# Option 1: Direct Python execution
python web_search_server.py

# Option 2: Use the batch script (Windows)
start_web_search.bat
```

### **Step 3: Configure MCP Client**
Add to your MCP client configuration:
```json
{
  "mcpServers": {
    "web-search": {
      "command": "python",
      "args": ["D:/Applications/java-spring-boot-secure-todo-app/mcp_server/web_search_server.py"],
      "env": {}
    }
  }
}
```

## 🔍 **Usage Examples**

### **1. General Web Search**
```json
{
  "tool": "search_web",
  "arguments": {
    "query": "Spring Boot best practices 2024",
    "engine": "duckduckgo",
    "num_results": 5
  }
}
```

### **2. News Search**
```json
{
  "tool": "search_news",
  "arguments": {
    "query": "Java 21 new features",
    "num_results": 3
  }
}
```

### **3. Technical Documentation Search**
```json
{
  "tool": "search_technical",
  "arguments": {
    "query": "Spring Boot MongoDB integration",
    "language": "Java"
  }
}
```

### **4. List Available Search Engines**
```json
{
  "tool": "get_search_engines",
  "arguments": {}
}
```

## 📊 **Tool Parameters**

### **search_web**
- **query** (required): Search query string
- **engine** (optional): Search engine to use (google, bing, duckduckgo)
- **num_results** (optional): Number of results (1-10, default: 5)

### **search_news**
- **query** (required): News search query
- **num_results** (optional): Number of articles (default: 5)

### **search_technical**
- **query** (required): Technical search query
- **language** (optional): Programming language focus (default: any)

## 🎯 **Use Cases**

### **1. Real-time Information**
- Current events and news
- Latest technology updates
- Market trends and analysis

### **2. Technical Research**
- Documentation lookup
- Code examples and tutorials
- Best practices and patterns

### **3. Problem Solving**
- Error troubleshooting
- Solution research
- Community discussions

### **4. Learning and Development**
- Tutorial discovery
- Course recommendations
- Expert opinions and insights

## 🔧 **Advanced Configuration**

### **Custom Search Engines**
You can extend the server by adding more search engines in the `WebSearchService` class:

```python
self.search_engines = {
    "google": "https://www.google.com/search",
    "bing": "https://www.bing.com/search",
    "duckduckgo": "https://duckduckgo.com/html",
    "your_custom_engine": "https://your-search-api.com/search"
}
```

### **API Integration**
For production use, replace the simulation with real search APIs:
- Google Custom Search API
- Bing Search API
- DuckDuckGo Instant Answer API
- SerpAPI
- SearchAPI

## 🚨 **Important Notes**

### **Current Implementation**
- **Simulation Mode**: The current implementation uses simulated results for demonstration
- **Production Ready**: Replace simulation with real search APIs for production use
- **Rate Limiting**: Implement rate limiting when using real APIs
- **API Keys**: Store API keys securely in environment variables

### **Privacy Considerations**
- DuckDuckGo is the default engine for privacy
- No search history is stored
- No personal data is collected
- Consider using VPN for additional privacy

## 🔄 **Integration with Other MCP Servers**

You can run multiple MCP servers simultaneously:

```json
{
  "mcpServers": {
    "web-search": {
      "command": "python",
      "args": ["web_search_server.py"]
    },
    "todo-monitoring": {
      "command": "python", 
      "args": ["todo_monitoring_server.py"]
    },
    "filesystem": {
      "command": "npx",
      "args": ["@modelcontextprotocol/server-filesystem", "/path/to/project"]
    }
  }
}
```

## 📈 **Performance Tips**

1. **Use appropriate num_results** - Don't request more results than needed
2. **Choose the right engine** - DuckDuckGo for privacy, Google for comprehensive results
3. **Cache results** - Implement caching for frequently searched terms
4. **Rate limiting** - Respect API rate limits when using real search APIs

## 🆘 **Troubleshooting**

### **Common Issues:**
1. **Server won't start** - Check Python installation and dependencies
2. **No results returned** - Verify search query format
3. **API errors** - Check API keys and rate limits
4. **Connection issues** - Verify network connectivity

### **Debug Mode:**
Enable debug logging by modifying the logging level:
```python
logging.basicConfig(level=logging.DEBUG)
```

## 🎉 **Success!**

Your Web Search MCP Server is now ready to provide real-time web search capabilities to AI assistants. You can search for current information, technical documentation, news, and much more!

**Next Steps:**
1. Test the search functionality
2. Integrate with your MCP client
3. Customize for your specific needs
4. Add real search API integration for production use
