@echo off
echo ========================================
echo  DVC Initialization
echo ========================================
echo.

cd /d "%~dp0\.."

echo Initializing DVC...
dvc init

echo.
echo Adding datasets to DVC tracking...
dvc add data/train
dvc add data/valid
dvc add data/test

echo.
echo Adding DVC files to git...
git add data/train.dvc data/valid.dvc data/test.dvc .dvc/.gitignore .dvc/config

echo.
echo ========================================
echo  DVC Initialized Successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Commit DVC files: git commit -m "Track dataset with DVC"
echo 2. Configure remote storage: dvc remote add -d myremote /path/to/storage
echo 3. Push data to remote: dvc push
echo.

pause
