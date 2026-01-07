@echo off
echo ========================================
echo Animal Detection - Backend Setup
echo ========================================
echo.

cd backend

echo Installing Maven dependencies...
call mvn clean install

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Backend setup completed successfully!
echo ========================================
echo.
echo To start the backend, run:
echo   cd backend
echo   mvn spring-boot:run
echo.
pause
