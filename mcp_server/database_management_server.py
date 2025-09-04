#!/usr/bin/env python3
"""
Database Management MCP Server
Provides database operations, analysis, and management capabilities
"""

import asyncio
import json
import logging
from datetime import datetime
from typing import Dict, Any, List, Optional
from mcp.server import Server
from mcp.types import Tool, TextContent
import pymongo
from pymongo import MongoClient
from bson import ObjectId
import pandas as pd
from io import StringIO

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DatabaseManager:
    """
    MCP Server for database management and analysis
    Provides comprehensive database operations for MongoDB
    """
    
    def __init__(self, connection_string: str = "mongodb://localhost:27017", database_name: str = "todoapp"):
        self.connection_string = connection_string
        self.database_name = database_name
        self.client = None
        self.db = None
        
    async def connect(self):
        """Connect to MongoDB"""
        try:
            self.client = MongoClient(self.connection_string)
            self.db = self.client[self.database_name]
            # Test connection
            self.client.admin.command('ping')
            logger.info(f"Connected to MongoDB: {self.database_name}")
            return True
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {e}")
            return False
    
    async def get_collections_info(self) -> Dict[str, Any]:
        """Get information about all collections"""
        if not self.db:
            return {"error": "Not connected to database"}
        
        collections = []
        for collection_name in self.db.list_collection_names():
            collection = self.db[collection_name]
            count = collection.count_documents({})
            collections.append({
                "name": collection_name,
                "document_count": count,
                "indexes": list(collection.list_indexes())
            })
        
        return {
            "database": self.database_name,
            "collections": collections,
            "total_collections": len(collections)
        }
    
    async def analyze_collection(self, collection_name: str) -> Dict[str, Any]:
        """Analyze a specific collection"""
        if not self.db:
            return {"error": "Not connected to database"}
        
        collection = self.db[collection_name]
        
        # Get sample documents
        sample_docs = list(collection.find().limit(5))
        
        # Get field statistics
        pipeline = [
            {"$project": {"arrayofkeyvalue": {"$objectToArray": "$$ROOT"}}},
            {"$unwind": "$arrayofkeyvalue"},
            {"$group": {"_id": "$arrayofkeyvalue.k", "count": {"$sum": 1}}},
            {"$sort": {"count": -1}}
        ]
        
        field_stats = list(collection.aggregate(pipeline))
        
        return {
            "collection_name": collection_name,
            "total_documents": collection.count_documents({}),
            "sample_documents": sample_docs,
            "field_statistics": field_stats,
            "indexes": list(collection.list_indexes())
        }
    
    async def execute_query(self, collection_name: str, query: Dict[str, Any], limit: int = 100) -> Dict[str, Any]:
        """Execute a MongoDB query"""
        if not self.db:
            return {"error": "Not connected to database"}
        
        try:
            collection = self.db[collection_name]
            results = list(collection.find(query).limit(limit))
            return {
                "collection": collection_name,
                "query": query,
                "results": results,
                "count": len(results)
            }
        except Exception as e:
            return {"error": f"Query failed: {str(e)}"}
    
    async def get_performance_stats(self) -> Dict[str, Any]:
        """Get database performance statistics"""
        if not self.db:
            return {"error": "Not connected to database"}
        
        try:
            # Get database stats
            db_stats = self.db.command("dbStats")
            
            # Get collection stats for each collection
            collection_stats = {}
            for collection_name in self.db.list_collection_names():
                collection = self.db[collection_name]
                stats = collection.aggregate([{"$collStats": {"storageStats": {}}}])
                collection_stats[collection_name] = list(stats)
            
            return {
                "database_stats": db_stats,
                "collection_stats": collection_stats,
                "timestamp": datetime.now().isoformat()
            }
        except Exception as e:
            return {"error": f"Failed to get performance stats: {str(e)}"}

# Initialize the database manager
db_manager = DatabaseManager()

# Create MCP Server
app = Server("database-management")

@app.list_tools()
async def list_tools() -> List[Tool]:
    """List available database management tools"""
    return [
        Tool(
            name="connect_database",
            description="Connect to the MongoDB database",
            inputSchema={
                "type": "object",
                "properties": {
                    "connection_string": {
                        "type": "string",
                        "description": "MongoDB connection string (default: mongodb://localhost:27017)"
                    },
                    "database_name": {
                        "type": "string", 
                        "description": "Database name (default: todoapp)"
                    }
                },
                "required": []
            }
        ),
        Tool(
            name="get_collections_info",
            description="Get information about all collections in the database",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="analyze_collection",
            description="Analyze a specific collection including field statistics and sample data",
            inputSchema={
                "type": "object",
                "properties": {
                    "collection_name": {
                        "type": "string",
                        "description": "Name of the collection to analyze"
                    }
                },
                "required": ["collection_name"]
            }
        ),
        Tool(
            name="execute_query",
            description="Execute a MongoDB query on a collection",
            inputSchema={
                "type": "object",
                "properties": {
                    "collection_name": {
                        "type": "string",
                        "description": "Name of the collection to query"
                    },
                    "query": {
                        "type": "object",
                        "description": "MongoDB query object"
                    },
                    "limit": {
                        "type": "number",
                        "description": "Maximum number of results (default: 100)"
                    }
                },
                "required": ["collection_name", "query"]
            }
        ),
        Tool(
            name="get_performance_stats",
            description="Get database performance statistics and storage information",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        Tool(
            name="find_todos_by_user",
            description="Find todos for a specific user with detailed analysis",
            inputSchema={
                "type": "object",
                "properties": {
                    "user_id": {
                        "type": "string",
                        "description": "User ID to search for"
                    }
                },
                "required": ["user_id"]
            }
        ),
        Tool(
            name="analyze_todo_patterns",
            description="Analyze todo patterns and statistics across all users",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        )
    ]

@app.call_tool()
async def call_tool(name: str, arguments: Dict[str, Any]) -> List[TextContent]:
    """Handle tool calls"""
    global db_manager
    
    if name == "connect_database":
        connection_string = arguments.get("connection_string", "mongodb://localhost:27017")
        database_name = arguments.get("database_name", "todoapp")
        
        db_manager = DatabaseManager(connection_string, database_name)
        success = await db_manager.connect()
        
        if success:
            return [TextContent(type="text", text="✅ Successfully connected to MongoDB database")]
        else:
            return [TextContent(type="text", text="❌ Failed to connect to MongoDB database")]
    
    elif name == "get_collections_info":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        data = await db_manager.get_collections_info()
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = f"📊 **Database Collections Overview**\n\n"
        result += f"**Database:** {data['database']}\n"
        result += f"**Total Collections:** {data['total_collections']}\n\n"
        
        for collection in data['collections']:
            result += f"**{collection['name']}**\n"
            result += f"- Documents: {collection['document_count']}\n"
            result += f"- Indexes: {len(collection['indexes'])}\n\n"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "analyze_collection":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        collection_name = arguments["collection_name"]
        data = await db_manager.analyze_collection(collection_name)
        
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = f"🔍 **Collection Analysis: {collection_name}**\n\n"
        result += f"**Total Documents:** {data['total_documents']}\n"
        result += f"**Indexes:** {len(data['indexes'])}\n\n"
        
        result += "**Field Statistics:**\n"
        for field in data['field_statistics'][:10]:  # Top 10 fields
            result += f"- {field['_id']}: {field['count']} documents\n"
        
        result += f"\n**Sample Documents:**\n"
        for i, doc in enumerate(data['sample_documents'][:3], 1):
            result += f"{i}. {json.dumps(doc, indent=2, default=str)}\n\n"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "execute_query":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        collection_name = arguments["collection_name"]
        query = arguments["query"]
        limit = arguments.get("limit", 100)
        
        data = await db_manager.execute_query(collection_name, query, limit)
        
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = f"🔍 **Query Results**\n\n"
        result += f"**Collection:** {data['collection']}\n"
        result += f"**Query:** {json.dumps(data['query'], indent=2)}\n"
        result += f"**Results Found:** {data['count']}\n\n"
        
        for i, doc in enumerate(data['results'][:5], 1):  # Show first 5 results
            result += f"**Result {i}:**\n{json.dumps(doc, indent=2, default=str)}\n\n"
        
        if data['count'] > 5:
            result += f"... and {data['count'] - 5} more results\n"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "get_performance_stats":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        data = await db_manager.get_performance_stats()
        
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = "📈 **Database Performance Statistics**\n\n"
        
        db_stats = data['database_stats']
        result += f"**Database Size:** {db_stats.get('dataSize', 0) / 1024 / 1024:.2f} MB\n"
        result += f"**Storage Size:** {db_stats.get('storageSize', 0) / 1024 / 1024:.2f} MB\n"
        result += f"**Index Size:** {db_stats.get('indexSize', 0) / 1024 / 1024:.2f} MB\n"
        result += f"**Total Collections:** {db_stats.get('collections', 0)}\n"
        result += f"**Total Objects:** {db_stats.get('objects', 0)}\n\n"
        
        result += "**Collection Details:**\n"
        for collection_name, stats in data['collection_stats'].items():
            if stats:
                stat = stats[0]
                result += f"- **{collection_name}:** {stat.get('count', 0)} documents\n"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "find_todos_by_user":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        user_id = arguments["user_id"]
        query = {"userId": user_id}
        
        data = await db_manager.execute_query("todos", query, 50)
        
        if "error" in data:
            return [TextContent(type="text", text=f"❌ {data['error']}")]
        
        result = f"📝 **Todos for User: {user_id}**\n\n"
        result += f"**Total Todos Found:** {data['count']}\n\n"
        
        if data['count'] > 0:
            # Analyze todo status
            status_counts = {}
            for todo in data['results']:
                status = todo.get('completed', False)
                status_key = 'Completed' if status else 'Pending'
                status_counts[status_key] = status_counts.get(status_key, 0) + 1
            
            result += "**Status Breakdown:**\n"
            for status, count in status_counts.items():
                result += f"- {status}: {count}\n"
            
            result += f"\n**Recent Todos:**\n"
            for i, todo in enumerate(data['results'][:5], 1):
                status_icon = "✅" if todo.get('completed', False) else "⏳"
                result += f"{i}. {status_icon} {todo.get('title', 'No title')}\n"
                if todo.get('description'):
                    result += f"   Description: {todo['description']}\n"
                result += f"   Created: {todo.get('createdAt', 'Unknown')}\n\n"
        else:
            result += "No todos found for this user.\n"
        
        return [TextContent(type="text", text=result)]
    
    elif name == "analyze_todo_patterns":
        if not db_manager.db:
            return [TextContent(type="text", text="❌ Not connected to database. Please connect first.")]
        
        # Get all todos
        all_todos = await db_manager.execute_query("todos", {}, 1000)
        
        if "error" in all_todos:
            return [TextContent(type="text", text=f"❌ {all_todos['error']}")]
        
        todos = all_todos['results']
        total_todos = len(todos)
        
        if total_todos == 0:
            return [TextContent(type="text", text="📊 No todos found in the database.")]
        
        # Analyze patterns
        completed_count = sum(1 for todo in todos if todo.get('completed', False))
        pending_count = total_todos - completed_count
        
        # Get unique users
        user_ids = set(todo.get('userId') for todo in todos if todo.get('userId'))
        
        result = f"📊 **Todo Patterns Analysis**\n\n"
        result += f"**Total Todos:** {total_todos}\n"
        result += f"**Completed:** {completed_count} ({completed_count/total_todos*100:.1f}%)\n"
        result += f"**Pending:** {pending_count} ({pending_count/total_todos*100:.1f}%)\n"
        result += f"**Active Users:** {len(user_ids)}\n\n"
        
        # Average todos per user
        if user_ids:
            avg_todos_per_user = total_todos / len(user_ids)
            result += f"**Average Todos per User:** {avg_todos_per_user:.1f}\n\n"
        
        # Most common words in titles
        title_words = []
        for todo in todos:
            if todo.get('title'):
                words = todo['title'].lower().split()
                title_words.extend(words)
        
        if title_words:
            from collections import Counter
            word_counts = Counter(title_words)
            common_words = word_counts.most_common(5)
            
            result += "**Most Common Words in Titles:**\n"
            for word, count in common_words:
                result += f"- '{word}': {count} times\n"
        
        return [TextContent(type="text", text=result)]
    
    else:
        return [TextContent(type="text", text=f"❌ Unknown tool: {name}")]

if __name__ == "__main__":
    asyncio.run(app.run())
