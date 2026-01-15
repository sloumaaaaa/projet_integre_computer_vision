@echo off
echo ========================================
echo  MLflow Server Startup
echo ========================================
echo.

cd /d "%~dp0"

echo Starting MLflow tracking server...
echo MLflow UI will be available at: http://localhost:5000
echo.
echo Press Ctrl+C to stop the server
echo.

mlflow server --backend-store-uri sqlite:///./mlflow.db --default-artifact-root ./mlruns --host 0.0.0.0 --port 5000

pause
