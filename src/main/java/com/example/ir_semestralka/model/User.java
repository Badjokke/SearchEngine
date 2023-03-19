package com.example.ir_semestralka.model;

public class User {
    private String name, password;
    private Boolean admin;


    public User(String name, String password, boolean admin){
        this.name = name;
        this.password = password;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getAdmin() {
        return admin;
    }
}
