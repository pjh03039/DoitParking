package com.example.myapplication;

public class ReservationClass {

    private String userID;
    private String reservationNumber;
    private String parkinglot_place;
    private String parkingareaID;
    private String resvDate;
    private String resvStartTime;
    private String resvEndTime;

    public ReservationClass() {}

    public ReservationClass(String userID, String reservationNumber, String parkinglot_place, String parkingareaID, String resvDate, String resvStartTime, String resvEndTime) {
        this.userID = userID;
        this.reservationNumber = reservationNumber;
        this.parkinglot_place = parkinglot_place;
        this.parkingareaID = parkingareaID;
        this.resvDate = resvDate;
        this.resvStartTime = resvStartTime;
        this.resvEndTime = resvEndTime;
    }

    public String getUserID() {
        return userID;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public String getParkinglot_place() {
        return parkinglot_place;
    }

    public String getParkingareaID() {
        return parkingareaID;
    }

    public String getResvDate() {
        return resvDate;
    }

    public String getResvStartTime() {
        return resvStartTime;
    }

    public String getResvEndTime() {
        return resvEndTime;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setParkinglot_place(String parkinglot_place) {
        this.parkinglot_place = parkinglot_place;
    }

    public void setParkingareaID(String parkingareaID) {
        this.parkingareaID = parkingareaID;
    }

    public void setResvDate(String resvDate) {
        this.resvDate = resvDate;
    }

    public void setResvStartTime(String resvStartTime) {
        this.resvStartTime = resvStartTime;
    }

    public void setResvEndTime(String resvEndTime) {
        this.resvEndTime = resvEndTime;
    }
}
