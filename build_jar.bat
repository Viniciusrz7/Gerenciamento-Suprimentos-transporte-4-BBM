@echo off
setlocal

set BASE=d:\Usuario\Desktop\Gerenciamento Suprimentos_transporte
set JDK=%BASE%\jdk17\jdk-17.0.11+9\bin
set JAR_OUT=%BASE%\BBM-Gerenciamento.jar
set JAR_TMP=%BASE%\target\jar_tmp
set CLASSES=%BASE%\target\classes

echo [BUILD] Compilando...
call "%BASE%\target\compile.bat" > "%BASE%\target\build.log" 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Compilacao falhou. JAR NAO foi atualizado.
    echo Veja o log em: %BASE%\target\build.log
    exit /b 1
)

echo [BUILD] Compilacao OK. Empacotando JAR...

if exist "%JAR_TMP%" rmdir /s /q "%JAR_TMP%"
mkdir "%JAR_TMP%\META-INF"

cd /d "%JAR_TMP%"
"%JDK%\jar.exe" xf "%BASE%\sqlite-jdbc-3.45.3.0.jar"
"%JDK%\jar.exe" xf "%BASE%\slf4j-stub.jar"
"%JDK%\jar.exe" xf "%BASE%\mysql-connector-j-8.3.0.jar"
xcopy /E /Y "%CLASSES%\*" "%JAR_TMP%\" > nul
xcopy /Y "%BASE%\src\main\resources\*.png" "%JAR_TMP%\" > nul 2>&1
xcopy /Y "%BASE%\src\main\resources\*.jpg" "%JAR_TMP%\" > nul 2>&1

echo Manifest-Version: 1.0> "%JAR_TMP%\META-INF\MANIFEST.MF"
echo Main-Class: com.bbm4.Main>> "%JAR_TMP%\META-INF\MANIFEST.MF"

"%JDK%\jar.exe" cfm "%JAR_OUT%" "%JAR_TMP%\META-INF\MANIFEST.MF" .
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao empacotar JAR.
    exit /b 1
)

echo [OK] JAR atualizado: %JAR_OUT%
endlocal
