#!/bin/bash

# 1. Move to Project Root
# This ensures the script runs from the project root, no matter where it's called from
cd "$(dirname "$0")/.." || exit

echo "========================================================"
echo "     Hypermarket System - Linux Debian (.deb) Builder"
echo "========================================================"

# 2. Build JAR with Maven Wrapper
echo ""
echo "[1/2] Compiling code and building JAR..."

# Check if wrapper exists, otherwise use system maven
if [ -f "./mvnw" ]; then
    ./mvnw clean package
else
    echo "[INFO] Wrapper not found, using system maven."
    mvn clean package
fi

# Check if build succeeded
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Maven Build Failed!"
    exit 1
fi

# 3. Create Debian Package
echo ""
echo "[2/2] Packaging Installer with jpackage..."

# Note: --name MUST be lowercase and no spaces for .deb
jpackage --input target \
         --name hypermarket-system-app \
         --app-version 1.0 \
         --main-jar HypermarketSystemApp.jar \
         --main-class com.hypermarket.app.Launcher \
         --type deb \
         --description "Hypermarket Management System" \
         --vendor "Abdallah R. Ali" \
         --linux-menu-group "Office" \
         --linux-shortcut \
         --icon "src/main/resources/com/hypermarket/images/cart.png"

# Check if jpackage succeeded
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Installer creation failed!"
    echo "Ensure you have 'fakeroot' and 'dpkg-deb' installed."
    exit 1
fi

# 4. Move to 'releases' folder (Overwrite if exists)
echo ""
echo "[INFO] Moving Installer to releases folder..."
mkdir -p releases

# Linux .deb files usually have version/arch in name (e.g. hypermarket-system-app_1.0-1_amd64.deb)
# We move any matching .deb file found in the root
if ls hypermarket-system-app*.deb 1> /dev/null 2>&1; then
    mv -f hypermarket-system-app*.deb releases/
    echo "[INFO] Installer moved successfully."
else
    echo "[ERROR] Installer file not found!"
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
echo "     File created in: releases/"
echo "========================================================"