@echo off
echo ========================================
echo Starting Standalone Frontend
echo ========================================
echo.
echo Opening frontend in your default browser...
echo.
echo Make sure the Spring Boot backend is running on port 8080!
echo.
start "" "index.html"
echo.
echo Frontend opened in browser!
echo The frontend will communicate with backend at http://localhost:8080
echo.
pause
