package com.ems.model;

import com.ems.model.exceptions.InvalidEmployeeDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Intern employee subtype. Demonstrates inheritance and method overriding.
public class Intern extends Employee {
    @JsonCreator
    public Intern(
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

    public Intern() throws InvalidEmployeeDataException {
        super(0, "", 0, "", 0, "", "");
    }

    @Override
    public double calculateSalary() {
        return getSalary();
    }

    @Override
    public String getEmployeeType() {
        return "INTERN";
    }

    @Override
    public String toString() {
        return String.format("%s, Type: Intern", super.toString());
    }
}