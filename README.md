# Hypermarket-Management-System

A desktop application designed to manage hypermarket operations, inventory, and staff.

## Prerequisites

Before running the build commands, ensure you have the following installed:

- **Java JDK 21** or higher
- **Apache Maven**

## Build Instructions

Follow these steps to build the application from source.

### 1. Build the JAR

Open your terminal in the project root folder and run:

```bash
mvn clean package
```

### 2. Create the Executable

This command bundles the application into a standalone folder with its own Java runtime. Run this **after** the build command succeeds:

**PowerShell**

```
jpackage --input target --name Hypermarket-Management-System-App --main-jar main-1.0-SNAPSHOT.jar --main-class com.hypermarket.app.Launcher --type app-image
```

Once finished, you will find a new folder named **`Hypermarket-Management-System-App`** in your project directory. Open it and run the `.exe` file inside.

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
