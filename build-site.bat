@echo off
REM Build script for the mycompany site application

set PROJECT_DIR=LegacyDemoSite\site
set BUILD_DIR=%PROJECT_DIR%\target\mycompany
set CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes
set LIB_DIR=%BUILD_DIR%\WEB-INF\lib
set SRC_DIR=%PROJECT_DIR%\src\main\java
set WEBAPP_DIR=%PROJECT_DIR%\src\main\webapp
set TOMCAT_WEBAPPS=tomcat-9.0.104\webapps

echo ========================================
echo Building mycompany application...
echo ========================================

REM Create build directories
echo Creating build directories...
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

REM Copy existing WAR contents as base
echo Copying existing WAR contents...
if exist "%LIB_DIR%\*" (
    echo Libraries already exist, skipping copy
) else (
    echo Copying libraries from existing build...
    xcopy /Y /E /I "%PROJECT_DIR%\target\mycompany\WEB-INF\lib" "%LIB_DIR%\" >nul 2>&1
)

REM Compile Java sources
echo Compiling Java sources...
javac -cp "%LIB_DIR%\*;tomcat-9.0.104\lib\servlet-api.jar" -d "%CLASSES_DIR%" "%SRC_DIR%\com\mycompany\controller\HomeController.java"

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    exit /b 1
)

REM Copy web resources
echo Copying web resources...
xcopy /Y /E /I "%WEBAPP_DIR%\*" "%BUILD_DIR%\" >nul 2>&1

REM Copy hibernate-fix.properties
echo Copying configuration files...
copy /Y hibernate-fix.properties "%CLASSES_DIR%\" >nul 2>&1

REM Create WAR file
echo Creating WAR file...
cd "%PROJECT_DIR%\target"
jar -cvf mycompany.war -C mycompany . >nul 2>&1
cd ..\..\..

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: WAR creation failed!
    exit /b 1
)

REM Deploy to Tomcat
echo Deploying to Tomcat...
copy /Y "%PROJECT_DIR%\target\mycompany.war" "%TOMCAT_WEBAPPS%\" >nul 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Deployment failed!
    exit /b 1
)

echo ========================================
echo Build completed successfully!
echo WAR file: %PROJECT_DIR%\target\mycompany.war
echo Deployed to: %TOMCAT_WEBAPPS%\mycompany.war
echo ========================================