package com.hfad.conf_me.models;

import java.util.Date;

public class ChatMessage {
    public String userID;
    public String textMessage;
    private long messageTime;

    public ChatMessage(){}

    public ChatMessage(String userID, String textMessage){
        this.userID = userID;
        this.textMessage = textMessage;
        this.messageTime = new Date().getTime();
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
