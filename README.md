[![Test and Build](https://github.com/mohammedrattia/Hypermarket-Management-System/actions/workflows/test_and_build.yml/badge.svg)](https://github.com/mohammedrattia/Hypermarket-Management-System/actions/workflows/test_and_build.yml)
[![GitHub Release](https://img.shields.io/github/v/release/mohammedrattia/Hypermarket-Management-System)](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest)

# 🛒 Hypermarket Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A full-featured desktop application designed to streamline retail operations. Built with **Java** and **JavaFX**, this system manages the entire flow of a hypermarket from the warehouse to the checkout counter.

---

## Table of Contents

- [About the Project](#-about-the-project)
- [Download & Install](#-download--install)
  - [Default Credentials](#-default-credentials)
- [System Modules](#-system-modules)
- [Technologies Used](#️-technologies-used)
- [For Developers](#-for-developers)
  - [Prerequisites](#prerequisites)
  - [Running from Source](#running-from-source)
  - [Building Executable (Windows/Linux)](#building-executables)

---

## 📖 About the Project

The **Hypermarket Management System** is an MVC-architected application that simulates real-world retail management. It solves the complexity of tracking inventory batches, managing employee roles, and processing sales transactions.

It uses a custom **File I/O DataStore** to persist data, making the application portable and lightweight.

---

## 📥 Download & Install

**Latest Version:** [Check Latest Release Notes](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest)

| Platform      | Type               | Download Link                                                                                                                                                                    |
| :------------ | :----------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Windows**   | Installer          | [📥 Hypermarket-System-App-Windows.exe](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-App-Windows.exe)             |
| **Windows**   | Portable           | [📦 Hypermarket-System-Portable-Windows.zip](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-Portable-Windows.zip)   |
| **Linux**     | Installer (Debian) | [📥 Hypermarket-System-App-Linux.deb](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-App-Linux.deb)                 |
| **Linux**     | Installer (RedHat) | [📥 Hypermarket-System-App-Linux.rpm](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-App-Linux.rpm)                 |
| **Linux**     | Portable           | [📦 Hypermarket-System-Portable-Linux.tar.gz](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-Portable-Linux.tar.gz) |
| **Universal** | JAR File           | [☕ Hypermarket-System-App-Universal.jar](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest/download/Hypermarket-System-App-Universal.jar)         |

### 🔑 Default Credentials

To access the system for the first time, use the default Administrator account:

| **Role**  | **Email**          | **Password** |
| --------- | ------------------ | ------------ |
| **Admin** | `admin@system.com` | `admin`      |

---

## 🚀 System Modules

### Admin Module

- **Employee Management:** Add, update, and delete employees with role-based access (Admin, Inventory, Sales, Marketing).
- **Dashboard Analytics:** View real-time KPIs, active user counts, and visualized data via Pie Charts.
- **Security:** Session-based authentication and secure password handling.

### Inventory Module

- **Product Management:** Create and edit products with images, categories, and descriptions.
- **Batch Tracking:** Track specific batches for expiration dates and delivery schedules.
- **Low Stock Alerts:** Automatic visual indicators for products falling below threshold levels.
- **Advanced Search:** Filter products by ID, Name, Category, or Price using reflection-based dynamic filtering.

### Sales Module

- **Point of Sale (POS):** Add items to a cart, calculate totals, and process transactions.
- **Receipt Generation:** Automatically generates PDF receipts for every order.
- **Order History:** View recent orders and daily revenue stats.
- **Returns Management:** Process product returns and update inventory automatically.

### Marketing Module

- **Offers & Promos:** Manage discounts on specific products.
- **Reports:** Generate sales and inventory reports.

---

## 🛠️ Technologies Used

- **Language:** [Java 25](https://www.oracle.com/java/technologies/downloads/#java25)
- **UI Framework:** [JavaFX](https://openjfx.io/)
- **Build Tool:** [Maven](https://maven.apache.org/)
- **Styling:** CSS (Custom stylesheets for every view)
- **Persistence:** Custom File I/O (Serialization & Text Parsing)
- **Libraries:**
  - `iText` (PDF Generation)
  - `JavaFX Controls`
  - `Ikonli` (Icons)

---

## 👨‍💻 For Developers

If you want to run or build the source code yourself directly from your IDE (VS Code, IntelliJ, Eclipse, Netbeans).

### Prerequisites

1. **Java JDK 25** (Ensure `JAVA_HOME` is set correctly).
2. **Apache Maven** (Optional if using the included `mvnw` wrapper).
3. **WiX Toolset v3.11** (Required only for building the Windows Installer `.exe`).

### Running from Source

1. **Clone the repository**

   ```bash
   git clone https://github.com/mohammedrattia/Hypermarket-Management-System.git
   cd Hypermarket-Management-System
   ```

2. **Run with Maven**

   ```bash
   # Linux / Mac
   ./mvnw javafx:run

   # Windows
   mvnw javafx:run
   ```

### Building Executables

The project includes automated scripts in the `scripts/` directory to handle building, packaging, and cleanup. All final output files will be automatically moved to the `releases/` folder.

#### 🪟 Windows

Run these scripts by double-clicking them or executing them in Command Prompt or PowerShell.

**1. Windows Installer (.exe)**
Builds the installer that sets up the app in `Program Files`, creates shortcuts, and adds it to the Start Menu.

```bat
scripts\build_windows_installer.bat
```

_Output:_ `releases\Hypermarket-System-App-Windows.exe`

**2. Windows Portable (No Install)**
Creates a standalone folder containing the executable and Java runtime. Useful for running from USB drives without installation.

```bat
scripts\build_windows_portable.bat
```

_Output:_ `releases\Hypermarket-System-Portable-Windows.zip` (or folder)

**3. Universal JAR**
Builds the cross-platform JAR file (requires Java to be installed on the target machine).

```bat
scripts\build_windows_jar.bat
```

_Output:_ `releases\Hypermarket-System-App-Universal.jar`

#### 🐧 Linux

Ensure scripts are executable first (`chmod +x scripts/*.sh`).

**1. Debian Package (.deb)**
Builds the standard installer package for Ubuntu, Debian, and Mint systems.

**Bash**

```
./scripts/build_linux_deb.sh
```

_Output:_ `releases/Hypermarket-System-App-Linux.deb`

**2. RedHat Package (.rpm)**
Builds the installer package for Fedora, RedHat, and CentOS systems.

**Bash**

```
./scripts/build_linux_rpm.sh
```

_Output:_ `releases/Hypermarket-System-App-Linux.rpm`

**3. Linux Portable (AppImage Folder)**
Creates a standalone application directory (zipped). Useful for generic Linux distros or creating AUR packages.

**Bash**

```
./scripts/build_linux_portable.sh
```

_Output:_ `releases/Hypermarket-System-Portable-Linux.tar.gz`

**4. Universal JAR**
Builds the cross-platform JAR file.

**Bash**

```
./scripts/build_linux_jar.sh
```

_Output:_ `releases\Hypermarket-System-App-Universal.jar`

---

## 📸 Screenshots

### Login Screen

![Login Screen](screenshots/login.png)
