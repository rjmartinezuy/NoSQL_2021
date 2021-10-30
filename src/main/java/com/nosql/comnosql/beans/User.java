package com.nosql.comnosql.beans;

import lombok.Data;

import java.util.ArrayList;

@Data
public class User {
    String email;
    String password;
    String name;
    String lastname;
    ArrayList<String> role;
    String id;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

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
        return role;
    }

    public void setRole(ArrayList<String> role) {
        this.role = role;
    }
}
