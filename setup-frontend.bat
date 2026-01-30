@echo off
echo ========================================
echo Animal Detection - Frontend Setup
echo ========================================
echo.

cd frontend

echo Installing npm dependencies...
call npm install

if %errorlevel% neq 0 (
    echo.
    echo ERROR: npm install failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Frontend setup completed successfully!
echo ========================================
echo.
echo To start the frontend, run:
echo   cd frontend
echo   npm start
echo.
pause
