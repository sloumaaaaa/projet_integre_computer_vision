@echo off
echo ============================================
echo  Compress Large 3D Models
echo ============================================
echo.
echo Your elephant.glb is 10.9 MB - too large!
echo Recommended: under 5 MB for fast loading
echo.
echo OPTION 1: Use Online Compressor (Easy)
echo   - Go to: https://products.aspose.app/3d/compression/glb
echo   - Upload elephant.glb
echo   - Compress and download
echo   - Replace original file
echo.

choice /C 12 /M "1=Open compressor  2=Skip for now"

if errorlevel 2 goto SKIP
if errorlevel 1 goto COMPRESS

:COMPRESS
echo Opening online compressor...
start https://products.aspose.app/3d/compression/glb
echo.
echo Also opening alternative:
start https://gltf.report/
echo.
echo INSTRUCTIONS:
echo 1. Upload your elephant.glb
echo 2. Apply compression/optimization
echo 3. Download compressed version
echo 4. Replace: assets\elephant.glb
echo 5. Reload the AR experience
echo.
pause
goto END

:SKIP
echo.
echo For now, the model will load but may take 15-30 seconds.
echo You can also try a smaller elephant model from Sketchfab.
echo.
pause

:END
