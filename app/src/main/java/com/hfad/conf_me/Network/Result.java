package com.hfad.conf_me.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("body")
    @Expose
    private String body;

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

}
