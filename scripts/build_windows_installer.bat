@echo off
setlocal

echo ========================================================
echo      Hypermarket System - Windows Installer Builder
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

REM 3. Create Installer
echo.
echo [2/2] Packaging Installer with jpackage...
jpackage --input target ^
         --name "Hypermarket System App" ^
         --java-options "--enable-native-access=javafx.graphics" ^
         --app-version 1.0 ^
         --main-jar HypermarketSystemApp.jar ^
         --main-class com.hypermarket.app.Launcher ^
         --type exe ^
         --win-dir-chooser ^
         --win-menu ^
         --win-shortcut ^
         --win-per-user-install ^
         --description "Hypermarket Management System" ^
         --vendor "Abdallah R. Ali" ^
         --icon "src\main\resources\com\hypermarket\images\cart.ico"

IF %ERRORLEVEL% NEQ 0 goto InstallerFailed

REM 4. Move to 'releases' folder (Overwrite if exists)
echo.
echo [INFO] Moving Installer to releases folder...

REM Create releases folder if it doesn't exist
if not exist "releases" mkdir releases

REM Move the file and rename it. /Y forces overwrite of existing file.
if exist "Hypermarket System App-1.0.exe" (
    move /Y "Hypermarket System App-1.0.exe" "releases\Hypermarket System App.exe"
    echo [INFO] Installer moved successfully.
) else (
    echo [ERROR] Installer file not found!
    goto InstallerFailed
)

goto Success

:BuildFailed
echo.
echo [ERROR] Maven Build Failed!
goto End

:InstallerFailed
echo.
echo [ERROR] Installer creation failed!
goto End

:Success
echo.
echo ========================================================
echo      SUCCESS! 
echo      File created: releases\Hypermarket System App.exe
echo ========================================================

REM 5. Cleanup Target Folder
echo.
echo [INFO] Cleaning up target directory...
call %MVN_CMD% clean
echo [INFO] Cleanup complete.

:End
popd
pause