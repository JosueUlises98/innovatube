@echo off
echo Ejecutando InnovaTube Frontend...
echo.

echo Instalando dependencias...
call npm install

echo Instalando ReCaptcha...
call npm install react-google-recaptcha --save

echo Ejecutando servidor de desarrollo en http://localhost:5173...
echo Backend disponible en http://localhost:8080
call npm run dev

pause 