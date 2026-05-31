@echo off
cd /d "%~dp0"
.\jdk17\jdk-17.0.11+9\bin\javaw.exe -cp "BBM-Gerenciamento.jar;sqlite-jdbc-3.45.3.0.jar;mysql-connector-j-8.3.0.jar;slf4j-stub.jar" com.bbm4.Main