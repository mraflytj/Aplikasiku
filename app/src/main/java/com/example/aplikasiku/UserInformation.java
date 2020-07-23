package com.example.aplikasiku;

public class UserInformation {
    private String name;
    private String email;
    private String phone;
    private String password;

    public UserInformation(){

    }

    public UserInformation(String name, String email, String phone, String password){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
