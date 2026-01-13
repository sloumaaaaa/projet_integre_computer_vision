@echo off
echo ============================================
echo  Checking for 3D Models in assets folder...
echo ============================================
echo.

cd /d "%~dp0"

set FOUND=0

if exist "assets\elephant.glb" (
    echo [OK] elephant.glb found
    set /a FOUND+=1
) else (
    echo [  ] elephant.glb missing
)

if exist "assets\tiger.glb" (
    echo [OK] tiger.glb found
    set /a FOUND+=1
) else (
    echo [  ] tiger.glb missing
)

if exist "assets\lion.glb" (
    echo [OK] lion.glb found
    set /a FOUND+=1
) else (
    echo [  ] lion.glb missing
)

if exist "assets\wolf.glb" (
    echo [OK] wolf.glb found
    set /a FOUND+=1
) else (
    echo [  ] wolf.glb missing
)

if exist "assets\pig.glb" (
    echo [OK] pig.glb found
    set /a FOUND+=1
) else (
    echo [  ] pig.glb missing
)

echo.
echo Found %FOUND% of 5 models
echo.

if %FOUND%==0 (
    echo No models found! Run download-models.bat to get started.
    echo.
    echo The 3D viewer works with placeholder models from CDN.
    pause
    exit /b
)

echo ============================================
echo  Models detected! Ready to use.
echo ============================================
echo.
echo To switch from CDN to local models:
echo.
echo 1. Open: index-ar.html in a text editor
echo 2. Find the function: get3DModelUrl
echo 3. Uncomment the local paths section
echo.
echo OR keep using CDN - both work!
echo.
echo ============================================
echo  Testing Model Viewer
echo ============================================
echo.
echo Opening AR experience...
start index-ar.html
echo.
echo Test Instructions:
echo 1. Switch to "AR Experience" mode
echo 2. Upload an animal image
echo 3. Wait for detection
echo 4. Click on the collected animal card
echo 5. 3D model viewer will open!
echo.
pause
