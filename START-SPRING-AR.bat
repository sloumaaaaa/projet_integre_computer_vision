@echo off
title Démarrage Backend Spring Boot + Frontend AR
color 0A

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║                                                        ║
echo ║        🚀 DÉMARRAGE AR ANIMAL DETECTION 🚀            ║
echo ║           (Spring Boot + Frontend Standalone)         ║
echo ║                                                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.

echo [Étape 1/2] Démarrage du backend Spring Boot...
echo.

cd backend

REM Démarrer Spring Boot dans une nouvelle fenêtre
start "Spring Boot Backend - Port 8080" cmd /k "mvn spring-boot:run"

echo ✓ Backend Spring Boot en cours de démarrage...
echo   (Attendez 20-30 secondes pour que le serveur démarre)
echo.

timeout /t 5 /nobreak >nul

echo [Étape 2/2] Ouverture du frontend AR...
echo.

cd ..
start "" "%CD%\frontend-standalone\index-ar.html"

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║                                                        ║
echo ║              ✓ APPLICATION LANCÉE! ✓                  ║
echo ║                                                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.
echo 📋 Ce qui tourne:
echo    • Backend Spring Boot: http://localhost:8080
echo    • Frontend AR: Ouvert dans le navigateur
echo.
echo ⏱️  IMPORTANT:
echo    Attendez 20-30 secondes que Spring Boot démarre
echo    avant d'utiliser l'application!
echo.
echo 🎮 Ensuite:
echo    1. Cliquez sur "AR Experience"
echo    2. Uploadez une image d'animal
echo    3. Commencez à collecter!
echo.
echo 💡 Images de test disponibles dans: images_de_test\
echo.
pause
