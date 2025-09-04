@echo off
echo Starting Web Search MCP Server...
echo.

REM Check if Python is available
python --version >nul 2>&1
if errorlevel 1 (
    echo Error: Python is not installed or not in PATH
    pause
    exit /b 1
)

REM Install requirements if needed
echo Installing requirements...
pip install -r web_search_requirements.txt

REM Start the web search MCP server
echo.
echo Starting Web Search MCP Server...
echo You can now use web search capabilities through MCP!
echo.
python web_search_server.py

pause
