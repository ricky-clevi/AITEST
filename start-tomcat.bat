@echo off
REM Start Tomcat server

echo Starting Tomcat...
cd tomcat-9.0.104\bin
call startup.bat

echo Tomcat started. Press Ctrl+C to stop.
pause