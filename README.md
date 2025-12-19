# Hypermarket-Management-System

A desktop application designed to manage hypermarket operations, inventory, and staff.

## Prerequisites

Before running the build commands, ensure you have the following installed:

- **Java JDK 25** (Ensure `JAVA_HOME` is set correctly).
- **Apache Maven**.
- **WiX Toolset v3.11** (Required only for building the Windows Installer `.exe`).

## Build Instructions

Follow these steps to build the application artifacts from source.

### 1. Compile & Package JAR

First, compile the code and create the "Fat JAR" (Universal JAR) that contains all dependencies.

```bash
mvn clean package
```

_Jar created:_ `target/main-1.0-SNAPSHOT.jar`

---

### 2. Create Native Installers (jpackage)

Once the JAR is built, you can use `jpackage` to create platform-specific installers.

#### 🪟 Windows: Installer (.exe)

_Requires WiX Toolset installed and added to PATH._

**PowerShell**

```
jpackage --input target --name Hypermarket-Setup --app-version 1.0 --main-jar main-1.0-SNAPSHOT.jar --main-class com.hypermarket.app.Launcher --type exe --win-dir-chooser --win-menu --win-shortcut --description "Hypermarket Management System" --vendor "Abdallah R. Ali" --icon "src\main\resources\com\hypermarket\images\cart.ico"
```

#### 🪟 Windows: Portable Folder (No Install)

Creates a standalone folder containing the `.exe` and runtime.

**PowerShell**

```
jpackage --input target --name Hypermarket-Management-System --app-version 1.0 --main-jar main-1.0-SNAPSHOT.jar --main-class com.hypermarket.app.Launcher --type app-image --description "Hypermarket Management System" --vendor "Abdallah R. Ali" --icon "src\main\resources\com\hypermarket\images\cart.ico"
```

#### 🐧 Linux

###### Debian Package (.deb)

Run this on Ubuntu/Debian.

**Bash**

```
jpackage --input target --name hypermarket-management-system --app-version 1.0 --main-jar main-1.0-SNAPSHOT.jar --main-class com.hypermarket.app.Launcher --type deb --description "Hypermarket Management System" --vendor "Abdallah R. Ali" --linux-menu-group "Office" --linux-shortcut --icon "src/main/resources/com/hypermarket/images/cart.png"
```

###### Protable App Image

Run this on Arch Linux.

Generates a standalone application directory. Useful for creating AUR packages or running portably.

**Bash**

```
jpackage --input target --name hypermarket-management-system --app-version 1.0 --main-jar main-1.0-SNAPSHOT.jar --main-class com.hypermarket.app.Launcher --type app-image --description "Hypermarket Management System" --vendor "Malek A. Abido" --icon "src/main/resources/com/hypermarket/images/cart.png"
```

## Default Credentials

To access the system for the first time, use the default Administrator account:

| **Role**  | **Email**      | **Password** |
| --------------- | -------------------- | ------------------ |
| **Admin** | `admin@system.com` | `admin`          |

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
