@echo off
echo ============================================
echo  Starting Local Web Server for AR Experience
echo ============================================
echo.
echo Server will run at: http://localhost:8000
echo.
echo IMPORTANT: Keep this window open!
echo Press Ctrl+C to stop the server
echo.
echo Starting server...
echo.

cd /d "%~dp0"
python -m http.server 8000

pause
