package com.nosql.comnosql.beans;

public class Student {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    String name;
    int age;
    String registrationNumber;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
