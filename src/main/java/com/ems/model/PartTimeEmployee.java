package com.ems.model;

import com.ems.model.exceptions.InvalidEmployeeDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Part-time employee subtype. Demonstrates inheritance and method overriding.
public class PartTimeEmployee extends Employee {
    @JsonCreator
    public PartTimeEmployee(
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

    public PartTimeEmployee() throws InvalidEmployeeDataException {
        super(0, "", 0, "", 0, "", "");
    }

    @Override
    public double calculateSalary() {
        return getSalary();
    }

    @Override
    public String getEmployeeType() {
        return "PARTTIME";
    }

    @Override
    public String toString() {
        return String.format("%s, Type: Part-Time", super.toString());
    }
}