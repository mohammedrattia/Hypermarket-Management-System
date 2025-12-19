# Hypermarket-Management-System

A desktop application designed to manage hypermarket operations, inventory, and staff.

## Prerequisites

Before running the build commands, ensure you have the following installed:

- **Java JDK 25** (Ensure `JAVA_HOME` is set correctly).
- **Apache Maven** (Optional if using the included `mvnw` wrapper).
- **WiX Toolset v3.11** (Required only for building the Windows Installer `.exe`).

## Build Instructions

The project includes automated scripts in the `scripts/` directory to handle building, packaging, and cleanup. All final output files will be automatically moved to the `releases/` folder.

### 🪟 Windows Users

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

### 🐧 Linux Users

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

## System Modules

The application is divided into four distinct modules based on user roles, ensuring secure and organized operations:

### 1. Admin Module

- **User Management:** Add, update, and remove employees.
- **System Oversight:** Monitor employees and manage system access.
- **Security:** Only Admins can create new accounts for other roles.

### 2. Sales Module

- **Point of Sale (POS):** Create new orders and process transactions.
- **Receipt Generation:** Automatically generates PDF receipts for customers.
- **Returns Management:** Handle item returns and calculate refunds based on original purchase price.

### 3. Inventory Module

- **Stock Management:** Add new products and monitor quantity levels.
- **Batch Tracking:** Track expiry dates and delivery dates for specific product batches.
- **Low Stock Alerts:** Dashboard highlights products falling below threshold levels.
- **Expiry Alerts:** Identifies batches expiring within the next 7 days.

### 4. Marketing Module

- **Offer Management:** Create and schedule discounts for products.
- **Reporting:** Generate reports on active offers, expired offers, and discount effectiveness.

---

## 📸 Screenshots

### Login Screen

![Login Screen](screenshots/login.png)
