package com.ems.model;

import com.ems.model.exceptions.InvalidEmployeeDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Full-time employee subtype. Demonstrates inheritance and method overriding.
// @JsonCreator configures the constructor for JSON deserialization.
public class FullTimeEmployee extends Employee {
    @JsonCreator
    public FullTimeEmployee(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("age") int age,
            @JsonProperty("address") String address,
            @JsonProperty("salary") double salary,
            @JsonProperty("department") String department,
            @JsonProperty("email") String email
    ) throws InvalidEmployeeDataException {
        super(id, name, age, address, salary, department, email);
    }

    public FullTimeEmployee() throws InvalidEmployeeDataException {
        super(0, "", 0, "", 0.0, "", "");
    }

    @Override
    public double calculateSalary() {
        // Return the base salary directly
        return getSalary();
    }

    @Override
    public String getEmployeeType() {
        return "FULLTIME";
    }

    @Override
    public String toString() {
        return String.format("%s, Type: Full-Time", super.toString());
    }
}