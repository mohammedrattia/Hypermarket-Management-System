@echo off
setlocal

echo ========================================================
echo      Hypermarket System - Universal JAR Builder
echo ========================================================

REM 1. Move to Project Root
pushd "%~dp0.."

REM 2. Check for Maven Wrapper
if exist mvnw.cmd goto UseWrapper
goto UseSystemMaven

:UseWrapper
set MVN_CMD=mvnw.cmd
echo [INFO] Using Maven Wrapper.
goto BuildJar

:UseSystemMaven
set MVN_CMD=mvn
echo [INFO] Maven Wrapper not found. Using system Maven.
goto BuildJar

:BuildJar
echo.
echo [1/2] Compiling code and building JAR...
call %MVN_CMD% clean package

IF %ERRORLEVEL% NEQ 0 goto BuildFailed

REM 3. Save JAR to releases folder (Overwrite)
echo.
echo [2/2] Saving JAR to "releases" folder...
if not exist "releases" mkdir releases

if exist "target\HypermarketSystemApp.jar" (
    copy /Y "target\HypermarketSystemApp.jar" "releases\HypermarketSystemApp.jar"
    echo [INFO] JAR copied successfully.
) else (
    echo [ERROR] JAR file not found in target!
    goto BuildFailed
)

REM 4. Cleanup Target Folder
echo.
echo [INFO] Cleaning up target directory...
call %MVN_CMD% clean
echo [INFO] Cleanup complete.

goto Success

:BuildFailed
echo.
echo [ERROR] Build Failed!
goto End

:Success
echo.
echo ========================================================
echo      SUCCESS! 
echo      File created: releases\HypermarketSystemApp.jar
echo ========================================================

:End
popd
pause