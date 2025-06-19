Employee Salary Management System (JavaFX Desktop App)

A powerful and user-friendly JavaFX desktop application designed to manage employee salary records efficiently. This system allows adding, updating, deleting, and searching employees with automatic salary calculations based on roles like Manager or Engineer.

🚀 Key Features
📋 Add Employees with automatic Employee ID (e.g., 101, 102…)

🧮 Salary Calculation based on:

Monthly Salary × 12

10% Allowance

Bonus for Managers or Overtime for Engineers

10% Tax Deduction

💾 Persistent Storage using .dat file (serialization)

🔍 Search, Update, Delete by Employee ID

📃 Show All Records in a pop-up window

🔁 Reset Form to clear all inputs

🎨 Styled GUI with a modern lite navy blue theme

✅ Input validation and error handling

🧩 Technologies Used
Java 17+ (JDK 24 Compatible)

JavaFX 23

Eclipse IDE

File I/O with Serialization

JavaFX Controls & Events

📂 Project Structure
cpp
Copy
Edit
Employee Salary Management System/
├── src/
│   └── gui/
│       ├── EmployeeSalaryGUI.java     // Main JavaFX Application
│       ├── Employee.java              // Abstract base class
│       ├── Manager.java               // Manager role
│       ├── Engineer.java              // Engineer role
│       └── EmployeeStorageUtil.java   // File I/O and utility logic
├── employees.dat                      // Data storage file
└── counter.txt                        // Tracks employee ID count

🔧 How to Run
Clone the repository:

bash
Copy
Edit
git clone https://github.com/your-username/employee-salary-management-system.git
Open in Eclipse or any IDE with JavaFX configured.

Run EmployeeSalaryGUI.java.

Ensure counter.txt and employees.dat are present or will be created automatically.

📌 Future Enhancements
Convert to .exe using jpackage for installation

Add charts or PDF report export

Optionally connect with MySQL or PostgreSQL

More advanced search and filter options
