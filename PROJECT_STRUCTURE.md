# Restaurant Management System - Project Structure

```
RestaurantMS/
├── src/
│   └── com/restaurant/
│       ├── Main.java                          # Entry point
│       ├── db/
│       │   └── DBConnection.java              # Singleton Pattern (Member 2)
│       ├── model/
│       │   ├── User.java
│       │   ├── Admin.java
│       │   ├── Waiter.java
│       │   ├── Chef.java
│       │   ├── Manager.java
│       │   ├── MenuItem.java
│       │   ├── FoodItem.java
│       │   ├── BeverageItem.java
│       │   ├── Order.java
│       │   ├── Table.java
│       │   ├── Bill.java
│       │   ├── Receipt.java
│       │   ├── SalesReport.java
│       │   ├── Inventory.java
│       │   └── Employee.java
│       ├── dao/
│       │   ├── MenuDAO.java
│       │   ├── OrderDAO.java
│       │   ├── TableDAO.java
│       │   ├── BillDAO.java
│       │   ├── EmployeeDAO.java
│       │   └── InventoryDAO.java
│       ├── controller/
│       │   ├── AdminController.java
│       │   ├── WaiterController.java
│       │   ├── ChefController.java
│       │   └── ManagerController.java
│       ├── view/
│       │   ├── LoginView.java
│       │   ├── MainFrame.java
│       │   ├── admin/
│       │   │   ├── AdminDashboard.java
│       │   │   ├── MenuManagementView.java
│       │   │   └── EmployeeManagementView.java
│       │   ├── waiter/
│       │   │   ├── WaiterDashboard.java
│       │   │   ├── OrderView.java
│       │   │   └── TableReservationView.java
│       │   ├── chef/
│       │   │   ├── ChefDashboard.java
│       │   │   ├── KitchenDisplayView.java
│       │   │   └── InventoryView.java
│       │   └── manager/
│       │       ├── ManagerDashboard.java
│       │       ├── BillingView.java
│       │       └── SalesReportView.java
│       ├── pattern/
│       │   ├── factory/
│       │   │   ├── MenuItemFactory.java       # Factory Pattern (Member 1)
│       │   │   └── MenuItemType.java
│       │   ├── observer/
│       │   │   ├── OrderObserver.java         # Observer Pattern (Member 3)
│       │   │   ├── OrderSubject.java
│       │   │   └── KitchenObserver.java
│       │   └── facade/
│       │       └── BillingFacade.java         # Facade Pattern (Member 4)
└── database/
    └── schema.sql
```
