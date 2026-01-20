@echo off
setlocal

rem HSQLDB 서버 시작 스크립트
set HSQLDB_JAR="%~dp0tomcat-9.0.104\lib\hsqldb-2.3.1.jar"
set DB_PATH="%~dp0hsqldb-data\broadleaf"
set DB_NAME=broadleaf

echo HSQLDB 서버를 시작합니다...
echo 데이터베이스 경로: %DB_PATH%
echo 데이터베이스 이름: %DB_NAME%

java -cp %HSQLDB_JAR% org.hsqldb.Server --database.0 file:%DB_PATH% --dbname.0 %DB_NAME%

pause