[![Build and Release](https://github.com/mohammedrattia/Hypermarket-Management-System/actions/workflows/build_and_release.yml/badge.svg)](https://github.com/mohammedrattia/Hypermarket-Management-System/actions/workflows/build_and_release.yml)

[Download Latest Version](https://github.com/mohammedrattia/Hypermarket-Management-System/releases/latest)

# 🛒 Hypermarket Management System

A full-featured desktop application designed to streamline retail operations. Built with **Java** and **JavaFX**, this system manages the entire flow of a hypermarket from the warehouse to the checkout counter.

---

## Table of Contents
* [About the Project](#-about-the-project)
* [System Modules](#-system-modules)
* [Technologies Used](#-technologies-used)
* [Installation & Usage](#-installation--usage)
    * [Prerequisites](#prerequisites)
    * [For Developers (Source Code)](#for-developers)
    * [Building Executable (Windows/Linux)](#building-executables)

---

## About the Project

The **Hypermarket Management System** is an MVC-architected application that simulates real-world retail management. It solves the complexity of tracking inventory batches, managing employee roles, and processing sales transactions.

It uses a custom **File I/O DataStore** to persist data, making the application portable and lightweight. (v1)

---

## 🚀 System Modules

### Admin Module
* **Employee Management:** Add, update, and delete employees with role-based access (Admin, Inventory, Sales, Marketing).
* **Dashboard Analytics:** View real-time KPIs, active user counts, and visualized data via Pie Charts.
* **Security:** Session-based authentication and secure password handling.

### Inventory Module
* **Product Management:** Create and edit products with images, categories, and descriptions.
* **Batch Tracking:** Track specific batches for expiration dates and delivery schedules.
* **Low Stock Alerts:** Automatic visual indicators for products falling below threshold levels.
* **Advanced Search:** Filter products by ID, Name, Category, or Price using reflection-based dynamic filtering.

### Sales Module
* **Point of Sale (POS):** Add items to a cart, calculate totals, and process transactions.
* **Receipt Generation:** Automatically generates PDF receipts for every order.
* **Order History:** View recent orders and daily revenue stats.
* **Returns Management:** Process product returns and update inventory automatically.

### Marketing Module
* **Offers & Promos:** Manage discounts on specific products.
* **Reports:** Generate sales and inventory reports.

---

## Technologies Used

* **Language:** [Java 25](https://www.oracle.com/java/technologies/downloads/#java25)
* **UI Framework:** [JavaFX](https://openjfx.io/)
* **Build Tool:** [Maven](https://maven.apache.org/)
* **Styling:** CSS (Custom stylesheets for every view)
* **Persistence:** Custom File I/O (Serialization & Text Parsing)
* **Libraries:**
    * `iText`
    * `JavaFX Controls`
    * `Ikonli`

---

## 📥 Installation & Usage

### Prerequisites
1.  **Java JDK 25** (Ensure `JAVA_HOME` is set correctly).
2.  **Apache Maven** (Optional if using the included `mvnw` wrapper).
3.  **WiX Toolset v3.11** (Required only for building the Windows Installer `.exe`).


### For Developers
If you want to run the code directly from your IDE (VS Code, IntelliJ, Eclipse):

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/mohammedrattia/Hypermarket-Management-System.git](https://github.com/mohammedrattia/Hypermarket-Management-System.git)
    cd Hypermarket-Management-System
    ```

2.  **Run with Maven**
    ```bash
    # Linux / Mac
    ./mvnw javafx:run

    # Windows
    mvnw javafx:run
    ```

### Building Executables
The project includes automated scripts in the `scripts/` directory to handle building, packaging, and cleanup. All final output files will be automatically moved to the `releases/` folder.

#### 🪟 Windows

Run these scripts by double-clicking them or executing them in Command Prompt/PowerShell.

**1. Windows Installer (.exe)** Builds the professional installer that sets up the app in `Program Files`, creates shortcuts, and adds it to the Start Menu.

```bat
scripts\build_windows_installer.bat
```

_Output:_ `releases\Hypermarket System App.exe`

**2. Windows Portable (No Install)** Creates a standalone folder containing the executable and Java runtime. Useful for running from USB drives without installation.

```bat
scripts\build_windows_portable.bat
```

_Output:_ `releases\Hypermarket System App\` folder

**3. Universal JAR** Builds the cross-platform JAR file (requires Java to be installed on the target machine).

```bat
scripts\build_jar.bat
```

_Output:_ `releases\HypermarketSystemApp.jar`

---

#### 🐧 Linux

Ensure scripts are executable first (`chmod +x scripts/*.sh`).

**1. Debian Package (.deb)** Builds the standard installer package for Ubuntu, Debian, and Mint systems.

```bash
./scripts/build_linux_deb.sh
```

_Output:_ `releases/hypermarket-system-app_1.0_amd64.deb`

**2. Linux Portable (AppImage Folder)** Creates a standalone application directory. Useful for generic Linux distros or creating AUR packages.

```bash
./scripts/build_linux_portable.sh
```

_Output:_ `releases/hypermarket-system-app/` folder

**3. Universal JAR** Builds the cross-platform JAR file.

```bash
./scripts/build_jar.sh
```

_Output:_ `releases/HypermarketSystemApp.jar`

## Default Credentials

To access the system for the first time, use the default Administrator account:

| **Role**  | **Email**          | **Password** |
| --------- | ------------------ | ------------ |
| **Admin** | `admin@system.com` | `admin`      |

---

## 📸 Screenshots

### Login Screen

![Login Screen](screenshots/login.png)
