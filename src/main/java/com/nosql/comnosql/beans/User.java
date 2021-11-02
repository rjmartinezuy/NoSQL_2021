package com.nosql.comnosql.beans;

import lombok.Data;

import java.util.ArrayList;

@Data
public class User {
    String email;
    String password;
    String name;
    String lastname;
    ArrayList<String> roles;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public ArrayList<String> getRole() {
        return roles;
    }

    public void setRole(ArrayList<String> roles) {
        this.roles = roles;
    }
}
