@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
echo Compiling Student Management System...
javac -encoding UTF-8 -d out src\main\java\com\weintern\sms\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed. Check the error messages above.
    pause
    exit /b 1
)
echo.
echo Starting Student Management System...
java -cp out com.weintern.sms.Main
pause
