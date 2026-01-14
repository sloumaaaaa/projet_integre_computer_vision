@echo off
echo ============================================
echo  Session Management System - Quick Test
echo ============================================
echo.
echo This will test your new session features!
echo.
echo Step 1: Starting backend...
start cmd /k "cd backend && mvn spring-boot:run"
echo.
echo Waiting for backend to start (15 seconds)...
timeout /t 15 /nobreak >nul
echo.
echo Step 2: Testing API endpoints...
echo.

curl -X POST http://localhost:8081/api/session/register -H "Content-Type: application/json" -d "{\"username\":\"demo\",\"email\":\"demo@example.com\",\"password\":\"demo123\"}" 2>nul

if %errorlevel%==0 (
    echo [OK] Registration endpoint working!
) else (
    echo [FAIL] Registration endpoint not responding
    echo Make sure backend is running on port 8081
)

echo.
echo Step 3: Opening frontend...
start index-ar.html
echo.
echo ============================================
echo  Test Instructions:
echo ============================================
echo.
echo 1. Register a new user:
echo    - Click "Register" button
echo    - Enter: demo / demo@test.com / demo123
echo.
echo 2. Play the game:
echo    - Switch to AR Experience mode
echo    - Upload an animal image
echo    - Earn points and collect animals
echo.
echo 3. Check leaderboard:
echo    - Scroll down to see your rank
echo    - Click refresh to update
echo.
echo 4. Test persistence:
echo    - Close and reopen the page
echo    - Click "Login" with same credentials
echo    - Your progress should be restored!
echo.
echo 5. Multi-user test:
echo    - Open in private/incognito window
echo    - Register different user
echo    - Both appear on leaderboard
echo.
pause
