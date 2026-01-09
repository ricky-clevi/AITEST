@echo off
rem ---------------------------------------------------------------------------
rem Set environment variables for Tomcat startup
rem ---------------------------------------------------------------------------

rem Set JVM options for proper encoding
set JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=MS949 -Dconsole.encoding=MS949 -Duser.language=ko -Duser.country=KR -Duser.timezone=Asia/Seoul -Dsun.stdout.encoding=MS949 -Dsun.stderr.encoding=MS949 -javaagent:%CATALINA_HOME%\springloaded-1.2.8.RELEASE.jar -noverify -Dspringloaded.debug=true -Dspringloaded.logLevel=DEBUG -Dproperty-override=%CATALINA_HOME%\webapps\mycompany\WEB-INF\classes\hibernate-fix.properties -Ddatabase.user=sa -Ddatabase.password= -Ddatabase.driver=org.hsqldb.jdbcDriver -Ddatabase.url=jdbc:hsqldb:hsql://localhost:9001/broadleaf -Druntime.environment=development

rem Set CATALINA_OPTS for additional Tomcat specific options
set CATALINA_OPTS=%CATALINA_OPTS%
