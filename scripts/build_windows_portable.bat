@echo off
setlocal

echo ========================================================
echo      Hypermarket System - Windows Portable Builder
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

REM 3. Create Portable App Image
echo.
echo [2/2] Creating Portable Folder with jpackage...
REM Note: We removed --win-menu, --win-shortcut, etc. because this is not an installer.
jpackage --input target ^
         --name "Hypermarket System App" ^
         --java-options "--enable-native-access=javafx.graphics" ^
         --app-version 1.0 ^
         --main-jar HypermarketSystemApp.jar ^
         --main-class com.hypermarket.app.Launcher ^
         --type app-image ^
         --description "Hypermarket Management System" ^
         --vendor "Abdallah R. Ali" ^
         --icon "src\main\resources\com\hypermarket\images\cart.ico"

IF %ERRORLEVEL% NEQ 0 goto ImageFailed

REM 4. Move to 'releases' folder (Overwrite if exists)
echo.
echo [INFO] Moving Portable Folder to releases...

REM Create releases folder if it doesn't exist
if not exist "releases" mkdir releases

REM If an old version of the folder exists in releases, delete it first
if exist "releases\Hypermarket System App" (
    echo [INFO] Removing old version in releases...
    rmdir /S /Q "releases\Hypermarket System App"
)

REM Move the new folder
if exist "Hypermarket System App" (
    move "Hypermarket System App" "releases\Hypermarket System App"
    echo [INFO] Portable folder moved successfully.
) else (
    echo [ERROR] Portable folder not found!
    goto ImageFailed
)

goto Success

:BuildFailed
echo.
echo [ERROR] Maven Build Failed!
goto End

:ImageFailed
echo.
echo [ERROR] Portable creation failed!
goto End

:Success
echo.
echo ========================================================
echo      SUCCESS! 
echo      Folder created: releases\Hypermarket System App
echo      (This folder contains the .exe and runs without installing)
echo ========================================================

REM 5. Cleanup Target Folder
echo.
echo [INFO] Cleaning up target directory...
call %MVN_CMD% clean
echo [INFO] Cleanup complete.

:End
popd
pause