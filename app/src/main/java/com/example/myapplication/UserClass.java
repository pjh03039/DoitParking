package com.example.myapplication;

public class UserClass {
    private String userID;
    private String userPW;
    private String userCell;
    private String userCarType;

    public UserClass() {}

    public UserClass(String userID, String userPW, String userCell, String userCarType) {
        this.userID = userID;
        this.userPW = userPW;
        this.userCell = userCell;
        this.userCarType = userCarType;
    }

    public String getUserID() {
        return userID.toLowerCase();
    }

    public String getUserPW() {
        return userPW;
    }

    public String getUserCell() {
        return userCell;
    }

    public String getUserCarType() {
        return userCarType;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public void setUserCell(String userCell) {
        this.userCell = userCell;
    }

    public void setUserCarType(String userCarType) {
        this.userCarType = userCarType;
    }
}
