@echo off
echo ========================================
echo   AR Features Quick Start Guide
echo ========================================
echo.
echo This will help you set up the AR features
echo.

echo [1/3] Checking Flask backend...
tasklist /FI "IMAGENAME eq python.exe" 2>NUL | find /I /N "python.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo ✓ Python is running
) else (
    echo ! Flask backend may not be running
    echo   Start it with: python app.py
)
echo.

echo [2/3] Checking Angular frontend...
tasklist /FI "IMAGENAME eq node.exe" 2>NUL | find /I /N "node.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo ✓ Node.js is running
) else (
    echo ! Angular frontend may not be running
    echo   Start it with: cd frontend ^&^& npm start
)
echo.

echo [3/3] Asset Status
echo.
echo Required 3D Models:
if exist "frontend\src\assets\models\elephant.glb" (
    echo ✓ elephant.glb
) else (
    echo ✗ elephant.glb - Missing
)

if exist "frontend\src\assets\models\lion.glb" (
    echo ✓ lion.glb
) else (
    echo ✗ lion.glb - Missing
)

if exist "frontend\src\assets\models\tiger.glb" (
    echo ✓ tiger.glb
) else (
    echo ✗ tiger.glb - Missing
)

if exist "frontend\src\assets\models\wolf.glb" (
    echo ✓ wolf.glb
) else (
    echo ✗ wolf.glb - Missing
)

if exist "frontend\src\assets\models\pig.glb" (
    echo ✓ pig.glb
) else (
    echo ✗ pig.glb - Missing
)

echo.
echo ========================================
echo   Next Steps:
echo ========================================
echo.
echo 1. Start Flask backend if not running:
echo    python app.py
echo.
echo 2. Start Angular frontend if not running:
echo    cd frontend
echo    npm start
echo.
echo 3. Download 3D models (see AR_FEATURES_README.md)
echo    Or test with placeholder images first
echo.
echo 4. Open browser: http://localhost:4200
echo.
echo 5. Click "AR Experience" button
echo.
echo 6. Upload an animal image and start collecting!
echo.
echo For detailed instructions, see AR_FEATURES_README.md
echo.
pause
