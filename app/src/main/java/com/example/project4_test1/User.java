package com.example.project4_test1;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userID")
    private String userID;
    @SerializedName("userPassword")
    private String userPassword;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
