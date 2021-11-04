package com.nosql.comnosql.beans;

import java.util.ArrayList;

public class RoleUpdater {
    String password;
    ArrayList<String> roles;
    String email;

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

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRole(ArrayList<String> roles) {
        this.roles = roles;
    }
}
