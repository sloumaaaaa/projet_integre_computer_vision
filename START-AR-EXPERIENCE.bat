@echo off
title AR Animal Detection - Quick Start
color 0A

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║                                                        ║
echo ║      🎮 AR ANIMAL DETECTION - QUICK START 🎮          ║
echo ║                                                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.
echo [Step 1/2] Starting Flask Backend...
echo.

REM Check if virtual environment exists
if exist .venv (
    echo ✓ Found virtual environment
    call .venv\Scripts\activate.bat
    echo ✓ Virtual environment activated
) else (
    echo ! No virtual environment found
    echo   Using global Python
)

REM Start Flask in a new window
start "Flask Backend - Animal Detection" cmd /k "python app.py"
timeout /t 3 /nobreak >nul

echo.
echo ✓ Flask backend starting...
echo   (Check the new window for backend status)
echo.
echo [Step 2/2] Opening AR Frontend...
echo.

REM Open the AR experience
start "" "%CD%\frontend-standalone\index-ar.html"

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║                                                        ║
echo ║              ✓ AR EXPERIENCE LAUNCHED! ✓              ║
echo ║                                                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.
echo Your browser should open automatically with the AR interface.
echo.
echo 📋 What's Running:
echo    • Flask Backend: http://localhost:5000
echo    • AR Frontend: Opened in browser
echo.
echo 🎮 How to Use:
echo    1. Click "AR Experience" button
echo    2. Upload an animal image
echo    3. Start collecting animals!
echo.
echo 🐾 Available Animals:
echo    🐷 Pig      - Common   (100 pts)
echo    🐘 Elephant - Uncommon (150 pts)
echo    🐺 Wolf     - Uncommon (175 pts)
echo    🦁 Lion     - Rare     (200 pts)
echo    🐯 Tiger    - Rare     (250 pts)
echo.
echo 💡 Tips:
echo    • Use images from 'images_de_test' folder to start
echo    • Build streaks by finding animals consecutively
echo    • Collect all 5 animals to unlock the Collector badge
echo.
echo Press any key to close this window (backend will keep running)
echo.
pause >nul
