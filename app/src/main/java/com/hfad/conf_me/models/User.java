package com.hfad.conf_me.models;

public class User {
    public String name, email, pass, phone, surname;

    public User() {}

    public User(String name, String email, String pass, String phone, String surname) {

        this.name= name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.surname = surname;

    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPass(){
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }



}
