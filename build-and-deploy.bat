@echo off
chcp 65001 >nul
echo ========================================
echo AITEST 프로젝트 빌드 및 배포 스크립트
echo ========================================
echo.

set PROJECT_DIR=%~dp0LegacyDemoSite
set TOMCAT_WEBAPPS=%~dp0tomcat-9.0.104\webapps

echo [1/4] Maven 빌드 시작...
cd /d "%PROJECT_DIR%"
call mvn clean package -DskipTests -U
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [오류] Maven 빌드 실패!
    pause
    exit /b 1
)
echo.
echo [성공] Maven 빌드 완료!
echo.

echo [2/4] 기존 WAR 파일 및 배포된 디렉토리 정리...
cd /d "%TOMCAT_WEBAPPS%"
if exist admin.war (
    del /F /Q admin.war
    echo   - admin.war 삭제 완료
)
if exist mycompany.war (
    del /F /Q mycompany.war
    echo   - mycompany.war 삭제 완료
)
if exist admin (
    rmdir /S /Q admin
    echo   - admin 디렉토리 삭제 완료
)
if exist mycompany (
    rmdir /S /Q mycompany
    echo   - mycompany 디렉토리 삭제 완료
)
if exist admin_extract (
    rmdir /S /Q admin_extract
    echo   - admin_extract 디렉토리 삭제 완료
)
echo.
echo [성공] 정리 완료!
echo.

echo [3/4] WAR 파일 배포...
copy /Y "%PROJECT_DIR%\admin\target\admin.war" "%TOMCAT_WEBAPPS%\admin.war"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [오류] admin.war 복사 실패!
    pause
    exit /b 1
)
echo   - admin.war 배포 완료

copy /Y "%PROJECT_DIR%\site\target\mycompany.war" "%TOMCAT_WEBAPPS%\mycompany.war"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [오류] mycompany.war 복사 실패!
    pause
    exit /b 1
)
echo   - mycompany.war 배포 완료
echo.
echo [성공] WAR 파일 배포 완료!
echo.

echo [4/4] 배포된 파일 확인...
dir "%TOMCAT_WEBAPPS%\admin.war" | findstr "admin.war"
dir "%TOMCAT_WEBAPPS%\mycompany.war" | findstr "mycompany.war"
echo.

echo ========================================
echo 빌드 및 배포가 성공적으로 완료되었습니다!
echo ========================================
echo.
echo 다음 단계:
echo 1. HSQLDB 시작: start-hsqldb.bat
echo 2. Tomcat 시작: start-tomcat.bat
echo 3. 애플리케이션 접속:
echo    - Admin: http://localhost:8080/admin/
echo    - Site:  http://localhost:8080/mycompany/
echo.
pause