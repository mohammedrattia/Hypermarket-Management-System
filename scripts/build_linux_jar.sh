#!/bin/bash

# 1. Move to Project Root
# This ensures the script runs from the project root, no matter where it's called from
cd "$(dirname "$0")/.." || exit

echo "========================================================"
echo "     Hypermarket System - Universal JAR Builder"
echo "========================================================"

# 2. Build JAR
echo ""
echo "[1/2] Compiling code and building JAR..."
./mvnw clean package

# Check if build succeeded
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Maven Build Failed!"
    exit 1
fi

# 3. Save JAR to releases folder
echo ""
echo "[2/2] Saving JAR to 'releases' folder..."
mkdir -p releases

if [ -f "target/HypermarketSystemApp.jar" ]; then
    cp "target/HypermarketSystemApp.jar" "releases/HypermarketSystemApp.jar"
    echo "[INFO] JAR copied successfully."
else
    echo "[ERROR] JAR file not found in target!"
    exit 1
fi

# 4. Cleanup Target Folder
echo ""
echo "[INFO] Cleaning up target directory..."
./mvnw clean
echo "[INFO] Cleanup complete."

echo ""
echo "========================================================"
echo "     SUCCESS!"
echo "     File created: releases/HypermarketSystemApp.jar"
echo "========================================================"