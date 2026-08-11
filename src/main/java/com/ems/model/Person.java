package com.ems.model;

import java.io.Serializable;

// Abstract base class for shared person information and behavior.
// Encapsulation: fields are private and accessed through getters/setters.
public abstract class Person implements Serializable {
    private String name;
    private int age;
    private String address;

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Abstract method for details
    public abstract String getDetails();

    @Override
    public String toString() {
        return String.format("Name: %s, Age: %d, Address: %s", name, age, address);
    }
}