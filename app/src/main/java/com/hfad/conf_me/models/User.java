package com.hfad.conf_me.models;

public class User {
    public String name, email, pass, phone, surname, description, user_id, tag1, tag2, tag3, nickname;

    public User() {}

    public User(String name, String email, String pass, String phone, String surname, String description, String user_id, String tag1, String tag2, String tag3, String nickname) {

        this.name= name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.surname = surname;
        this.description = description;
        this.user_id = user_id;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;


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

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
