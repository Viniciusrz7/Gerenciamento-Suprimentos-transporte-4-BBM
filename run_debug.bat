@echo off
cd /d "%~dp0"
echo Iniciando aplicacao...
echo Diretorio: %CD%
.\jdk17\jdk-17.0.11+9\bin\java.exe -cp "BBM-Gerenciamento.jar;sqlite-jdbc-3.45.3.0.jar;slf4j-stub.jar" com.bbm4.Main
echo.
echo Aplicacao encerrada. Pressione qualquer tecla para fechar.
pause
