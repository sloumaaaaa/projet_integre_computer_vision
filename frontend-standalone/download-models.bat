@echo off
echo ============================================
echo  3D Animal Models - Quick Download
echo ============================================
echo.
echo This script will help you download free 3D animal models
echo.
echo OPTION 1: Download Quaternius Ultimate Animals Pack (Recommended)
echo   - Free, high quality, low poly
echo   - Includes all animals we need
echo.
echo OPTION 2: Manual download from Sketchfab
echo   - Go to: https://sketchfab.com/
echo   - Search for each animal
echo   - Filter by: Free + Downloadable
echo   - Download as GLB format
echo.
echo OPTION 3: Use AI to generate (Meshy.ai)
echo   - Go to: https://www.meshy.ai/
echo   - Generate 3D models from text
echo   - Free tier available
echo.

choice /C 123 /M "Choose an option (1/2/3)"

if errorlevel 3 goto OPTION3
if errorlevel 2 goto OPTION2
if errorlevel 1 goto OPTION1

:OPTION1
echo.
echo Opening Quaternius download page...
start https://quaternius.com/packs/ultimateanimals.html
echo.
echo INSTRUCTIONS:
echo 1. Click "Download" on the page
echo 2. Extract the ZIP file
echo 3. Find these files and copy to: assets\
echo    - Elephant model ^> elephant.glb
echo    - Tiger model ^> tiger.glb
echo    - Lion model ^> lion.glb
echo    - Wolf model ^> wolf.glb
echo    - Pig model ^> pig.glb
echo 4. Run: update-models.bat
echo.
pause
goto END

:OPTION2
echo.
echo Opening Sketchfab...
start https://sketchfab.com/search?features=downloadable^&licenses=322a749bcfa841b29dff1e8a1bb74b0b^&q=elephant+low+poly^&type=models
echo.
echo INSTRUCTIONS:
echo 1. Search for: elephant, tiger, lion, wolf, pig
echo 2. Filter: Free + Downloadable
echo 3. Download each as GLB format
echo 4. Save to: assets\
echo 5. Rename files: elephant.glb, tiger.glb, etc.
echo 6. Run: update-models.bat
echo.
pause
goto END

:OPTION3
echo.
echo Opening Meshy AI...
start https://www.meshy.ai/
echo.
echo INSTRUCTIONS:
echo 1. Sign up for free account
echo 2. Use Text-to-3D feature
echo 3. Generate: "low poly elephant", "low poly tiger", etc.
echo 4. Download as GLB
echo 5. Save to: assets\
echo 6. Run: update-models.bat
echo.
pause
goto END

:END
echo.
echo ============================================
echo  Model Viewer is already working!
echo ============================================
echo.
echo You can test it now with placeholder models:
echo - Open: index-ar.html
echo - Switch to AR Experience
echo - Upload animal image
echo - Click collected animal to view 3D
echo.
echo After downloading real models, they will
echo automatically replace the placeholders.
echo.
pause
