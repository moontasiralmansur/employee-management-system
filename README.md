# Employee Management System

A Java-based GUI Employee Management System developed for CSE2104 (Object Oriented Programming Lab) at the University of Liberal Arts Bangladesh (ULAB).

## Academic Context

This is an academic project developed as part of my coursework at the University of Liberal Arts Bangladesh (ULAB). I am preserving my academic projects on GitHub as a record of my learning journey, coursework, and progression throughout my studies.

This repository is intended for educational and archival purposes.

## Academic Information

- **Author:** Moontasir Al Mansur
- **Institution:** University of Liberal Arts Bangladesh (ULAB)
- **Course:** CSE2104 (Object Oriented Programming Lab)

## Project Overview

Employee Management System is a desktop application with a Swing-based GUI that lets an administrator manage employee records through a dashboard. After logging in, the user can add, edit, delete, search, and view employees of different employment types. Records are validated before being stored and are persisted locally as JSON, so data survives application restarts.

The application is structured into three layers: model classes representing employees and users, controller classes handling authentication, business rules, and persistence, and view classes providing the Swing interface.

## Features

- **User login** — authentication against configured default credentials (admin / admin1234), with saved-credential fallback via `config/users.json`
- **Dashboard** — central window with the employee table, action buttons, and a live employee count in the status bar
- **Adding employees** — form dialog with an auto-generated read-only employee ID
- **Editing employees** — same form pre-filled with the selected employee's data
- **Deleting employees** — with a confirmation prompt before removal
- **Viewing employee records** — read-only table showing ID, Name, Age, Type, Department, Email, and Salary
- **Employee search** — by exact employee ID or by partial, case-insensitive name match, with results shown in a separate dialog where the user can also edit or delete
- **Different employee types** — Full-Time, Part-Time, and Intern subtypes
- **Employee validation** — required fields, age between 18 and 70, positive salary, and non-duplicate IDs
- **JSON persistence** — employees are saved to and loaded from `data/employees.json` using Jackson
- **About dialog** — project and author information
- **Logout** — returns the user to the login screen

## Technologies Used

- **Java 21** — language and runtime (project targets JDK 21)
- **Java Swing** — GUI toolkit (windows, dialogs, tables, custom button component)
- **Maven 3.9+** — build and dependency management
- **Jackson 2.15.2** — JSON serialization/deserialization (`jackson-databind`), including polymorphic subtype support for employee types
- **Log4j2 2.20.0** — application logging (`log4j-api`, `log4j-core`), configured via `src/main/resources/log4j2.xml`
- **JUnit 5.10.0** — test framework dependency included in the build configuration

## OOP Concepts Used

- **Classes and Objects** — every entity and screen in the application is modeled as a class and instantiated as objects
- **Encapsulation** — private fields with public getters/setters in `Person`, `Employee`, and `User`; fields are only reachable through controlled accessors
- **Inheritance** — `Person` → `Employee` → `FullTimeEmployee`/`PartTimeEmployee`/`Intern`; Swing components are extended as well (`JFrame` → `LoginFrame`/`DashboardFrame`, `JButton` → `ModernButton`)
- **Abstraction** — abstract classes `Person` (`getDetails()`) and `Employee` (`calculateSalary()`, `getEmployeeType()`) define the contract, leaving implementation to subclasses
- **Polymorphism** — `Employee` references are assigned concrete subtypes at runtime (e.g., in `EmployeeFormDialog`), and `EmployeeController` operates uniformly on a `List<Employee>`
- **Method Overriding** — subclasses override `toString()`, `getDetails()`, `calculateSalary()`, `getEmployeeType()`, and `ModernButton` overrides `paintComponent()` for custom rendering
- **Constructor Overloading** — `Employee` and its subtypes provide parameterized and no-argument constructors; `EmployeeFormDialog` serves both add and edit modes through one constructor with a nullable employee
- **Composition / Association** — `EmployeeController` owns a `FileHandler` and a `List<Employee>`; `DashboardFrame` composes `EmployeeTablePanel` and `EmployeeController`; `LoginFrame` associates with `AuthController`
- **Access Modifiers** — private fields, public APIs, and protected overrides are used consistently to control visibility
- **Singleton Pattern** — `CredentialsManager` exposes a single shared instance through `getInstance()`

## Requirements

- JDK 21
- Maven 3.9 or newer
- A desktop environment compatible with Java Swing (Windows, macOS, or Linux with a GUI)

## How to Run

1. Make sure JDK 21 and Maven are installed and available on the command line.
2. Open a terminal in the project directory (`employee-management-system`).
3. Build the project:

   ```text
   mvn clean package
   ```

4. Run the packaged application from the project directory:

   ```text
   java -jar target/ems-1.0.jar
   ```

5. Log in with the default credentials: username `admin`, password `admin1234`.

Note: run the application from the project root, because data files are resolved relative to the working directory.

## GUI Overview

- **Login window** — the application starts here; pressing Enter in a field triggers login, and an Exit button closes the application
- **Dashboard** — a dark header with the title and a Logout button, a left control panel with Add Employee, Search Employee, Edit Employee, Delete Employee, Refresh, and About EMS buttons, a central employee table, and a status bar showing the total employee count
- **Employee form dialog** — used for both adding and editing; fields for employee type, ID (read-only), name, age, email, address, department, and salary, with validation messages shown as dialogs
- **Search window** — choose Employee ID or Employee Name, enter the term, and search; results open in a dialog where the selected row can be edited or deleted directly
- **About dialog** — shows the project name and developer credit

## How Data Is Stored

- Employee records are stored in `data/employees.json`. Jackson writes the record list with pretty printing, and polymorphic type information (`employeeType`: FULLTIME, PARTTIME, or INTERN) lets the correct subtype be restored on load.
- Data is loaded at startup and saved after every add, update, or delete through `FileHandler`.
- Login credentials are stored in `config/users.json` after a successful login and are checked as a fallback if they differ from the defaults.
- Runtime logging is written by Log4j2 to the console and to `logs/ems.log`.

## Project Structure

```text
employee-management-system/
├── pom.xml
├── .gitignore
├── config/
│   └── users.json
├── data/
│   └── employees.json
└── src/
    ├── main/
    │   ├── java/com/ems/
    │   │   ├── Main.java
    │   │   ├── controller/
    │   │   │   ├── AuthController.java
    │   │   │   ├── CredentialsManager.java
    │   │   │   ├── EmployeeController.java
    │   │   │   └── FileHandler.java
    │   │   ├── model/
    │   │   │   ├── Employee.java
    │   │   │   ├── FullTimeEmployee.java
    │   │   │   ├── Intern.java
    │   │   │   ├── PartTimeEmployee.java
    │   │   │   ├── Person.java
    │   │   │   ├── User.java
    │   │   │   └── exceptions/
    │   │   │       └── InvalidEmployeeDataException.java
    │   │   └── view/
    │   │       ├── DashboardFrame.java
    │   │       ├── EmployeeFormDialog.java
    │   │       ├── EmployeeTablePanel.java
    │   │       ├── LoginFrame.java
    │   │       ├── SearchDialog.java
    │   │       ├── SearchFrame.java
    │   │       └── components/
    │   │           └── ModernButton.java
    │   └── resources/
    │       ├── application.properties
    │       └── log4j2.xml
    └── test/java/com/ems/
```

## Limitations

- Data is stored in a plain JSON file rather than a database, so there is no multi-user concurrency control
- Only a single administrator account is supported
- Passwords are saved in plaintext in `config/users.json`
- Email input is only checked for being non-empty, not for format
- Search operates on the in-memory record list loaded at startup
- Employee IDs are randomly generated rather than sequentially assigned
- No automated tests are included yet, although the JUnit dependency is configured

## Future Improvement Possibilities

- Replace JSON file storage with a database (e.g., SQLite or MySQL)
- Add password hashing and account management with multiple users and roles
- Add email format validation and stronger field-level checks
- Implement sequential ID generation or a persistent ID counter
- Add reporting features such as export to CSV/PDF and salary summaries
- Add unit and integration tests for controllers and persistence
- Improve accessibility and internationalization of the GUI

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
