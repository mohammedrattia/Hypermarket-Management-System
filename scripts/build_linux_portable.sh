#!/bin/bash

# 1. Move to Project Root
cd "$(dirname "$0")/.." || exit

echo "========================================================"
echo "     Hypermarket System - Linux Portable Builder"
echo "========================================================"

# 2. Build JAR
echo ""
echo "[1/2] Compiling code and building JAR..."

if [ -f "./mvnw" ]; then
    ./mvnw clean package
else
    echo "[INFO] Wrapper not found, using system maven."
    mvn clean package
fi

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Maven Build Failed!"
    exit 1
fi

# 3. Create Portable App Image (Folder)
echo ""
echo "[2/2] Creating Portable Folder with jpackage..."

# Note: We use lowercase name to stay safe on Linux
jpackage --input target \
         --name hypermarket-system-app \
         --java-options "--enable-native-access=javafx.graphics" \
         --app-version 1.0 \
         --main-jar HypermarketSystemApp.jar \
         --main-class com.hypermarket.app.Launcher \
         --type app-image \
         --description "Hypermarket Management System" \
         --vendor "Abdallah R. Ali" \
         --icon "src/main/resources/com/hypermarket/images/cart.png"

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Portable creation failed!"
    exit 1
fi

# 4. Move to 'releases' folder (Overwrite)
echo ""
echo "[INFO] Moving Portable Folder to releases..."
mkdir -p releases

# Remove old version if it exists
if [ -d "releases/hypermarket-system-app" ]; then
    echo "[INFO] Removing old version in releases..."
    rm -rf "releases/hypermarket-system-app"
fi

# Move the new folder
if [ -d "hypermarket-system-app" ]; then
    mv "hypermarket-system-app" "releases/"
    echo "[INFO] Portable folder moved successfully."
else
    echo "[ERROR] Portable folder not found!"
    exit 1
fi

# 5. Cleanup Target Folder
echo ""
echo "[INFO] Cleaning up target directory..."
if [ -f "./mvnw" ]; then
    ./mvnw clean
else
    mvn clean
fi
echo "[INFO] Cleanup complete."

echo ""
echo "========================================================"
echo "     SUCCESS!"
echo "     Folder created: releases/hypermarket-system-app"
echo "========================================================"