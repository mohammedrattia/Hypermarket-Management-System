HyperMarket-Project/
├── data/ <-- FOLDER FOR TEXT FILES (Read/Write)
│ ├── users.txt
│ ├── products.txt
│ └── orders.txt
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── hypermarket/
│ │ ├── app/ <-- ENTRY POINT
│ │ │ ├── Main.java
│ │ │ └── Launcher.java
│ │ │
│ │ ├── model/ <-- DATA OBJECTS (From Diagram)
│ │ │ ├── User.java
│ │ │ ├── Product.java
│ │ │ ├── Order.java
│ │ │ ├── ... (All entity classes)
│ │ │
│ │ ├── data/ <-- DATA MANAGEMENT
│ │ │ ├── DataStore.java
│ │ │ └── FileManager.java
│ │ │
│ │ ├── service/ <-- BUSINESS LOGIC & AUTH
│ │ │ ├── Auth.java
│ │ │ └── Session.java
│ │ │
│ │ └── features/ <-- THE MODULES
│ │ ├── admin/
│ │ │ ├── AdminController.java
│ │ │ └── ManageEmployeesHelper.java
│ │ │
│ │ ├── sales/
│ │ │ ├── SalesController.java
│ │ │ └── CartLogic.java
│ │ │
│ │ ├── inventory/
│ │ │ └── InventoryController.java
│ │ │
│ │ └── marketing/
│ │ └── MarketingController.java
│ │
│ └── resources/ <-- UI ASSETS
│ └── com/
│ └── hypermarket/
│ ├── view/ <-- FXML FILES
│ │ ├── admin/ <-- MATCHING FOLDERS IN RESOURCES
│ │ │ └── admin_view.fxml
│ │ ├── sales/
│ │ │ └── sales_view.fxml
│ │
│ ├── style/ <-- CSS FILES
│ │ ├── style.css
│ │ └── theme.css
│ │
│ └── images/ <-- ICONS & LOGOS
│ ├── logo.png
│ └── user_icon.png
│
└── pom.xml
