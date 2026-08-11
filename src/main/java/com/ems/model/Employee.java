package com.ems.model;

import com.ems.model.exceptions.InvalidEmployeeDataException;
import com.fasterxml.jackson.annotation.*;

// Abstract employee base class extending Person.
// Abstraction: subclasses must implement calculateSalary() and getEmployeeType().
// Jackson annotations enable polymorphic JSON serialization of employee subtypes.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "employeeType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeEmployee.class, name = "FULLTIME"),
        @JsonSubTypes.Type(value = PartTimeEmployee.class, name = "PARTTIME"),
        @JsonSubTypes.Type(value = Intern.class, name = "INTERN")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Employee extends Person {
    private int id;
    private double salary;
    private String department;
    private String email;

    // Default constructor for Jackson
    public Employee() {
        super("", 0, "");
        this.id = 0;
        this.salary = 0;
        this.department = "";
        this.email = "";
    }

    public Employee(int id, String name, int age, String address,
                    double salary, String department, String email) throws InvalidEmployeeDataException {
        super(name, age, address);

        // Data validation
        if (id <= 0) {
            throw new InvalidEmployeeDataException("Employee ID must be positive");
        }
        if (salary < 0) {
            throw new InvalidEmployeeDataException("Salary cannot be negative");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Name cannot be empty");
        }

        this.id = id;
        this.salary = salary;
        this.department = department;
        this.email = email;
    }

    // Getters and setters with validation
    public int getId() {
        return id;
    }

    public void setId(int id) throws InvalidEmployeeDataException {
        if (id <= 0) {
            throw new InvalidEmployeeDataException("Employee ID must be positive");
        }
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) throws InvalidEmployeeDataException {
        if (salary < 0) {
            throw new InvalidEmployeeDataException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Abstract methods for employee type
    public abstract double calculateSalary();
    public abstract String getEmployeeType();

    @Override
    public String getDetails() {
        return String.format("ID: %d, Name: %s, Department: %s, Salary: %.2f",
                id, getName(), department, salary);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Age: %d, Department: %s, Salary: %.2f",
                id, getName(), getAge(), department, salary);
    }
}