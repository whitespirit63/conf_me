package com.hfad.conf_me.models;

public class DialogItem {
    public String userName;
    public String userSurname;
    public String userID;
    public String lastMessage;

    public DialogItem(){}

    public DialogItem(String userName, String userSurname, String userID, String lastMessage){
        this.userName = userName;
        this.userSurname = userSurname;
        this.userID = userID;
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}