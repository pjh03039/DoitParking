package com.example.myapplication;

public class ParkingClass {

    private String prkplceNm;
    private String prkplceNo;
    private String prkplceSe;
    private String prkplceType;
    private String rdnmadr;
    private String lnmadr;
    private String prkcmprt;
    private String operDay;
    private String weekdayOperOpenHhmm;
    private String weekdayOperCloseHhmm;
    private String satOperOpenHhmm;
    private String satOperCloseHhmm;
    private String holidayOperOpenHhmm;
    private String holidayOperCloseHhmm;
    private String parkingchrgeInfo;
    private String basicTime;
    private String basicCharge;
    private String addUnitTime;
    private String addUnitCharge;
    private String phoneNumber;
    private String latitude;
    private String longitude;
    private String parkingareaID;

    public String getPrkplceNm() {
        return prkplceNm;
    }

    public void setPrkplceNm(String prkplceNm) {
        this.prkplceNm = prkplceNm;
    }

    public String getPrkplceNo() { return prkplceNo; }

    public void setPrkplceNo(String prkplceNo) {
        this.prkplceNo = prkplceNo;
    }

    public String getPrkplceSe() {
        return prkplceSe;
    }

    public void setPrkplceSe(String prkplceSe) {
        this.prkplceSe = prkplceSe;
    }

    public String getPrkplceType() {
        return prkplceType;
    }

    public void setPrkplceType(String prkplceType) {
        this.prkplceType = prkplceType;
    }

    public String getRdnmadr() {
        return rdnmadr;
    }

    public void setRdnmadr(String rdnmadr) {
        this.rdnmadr = rdnmadr;
    }

    public String getLnmadr() { return lnmadr; }

    public void setLnmadr(String lnmadr) { this.lnmadr = lnmadr; }

    public String getPrkcmprt() {
        return prkcmprt;
    }

    public void setPrkcmprt(String prkcmprt) {
        this.prkcmprt = prkcmprt;
    }

    public String getOperDay() {
        return operDay;
    }

    public void setOperDay(String operDay) {
        this.operDay = operDay;
    }

    public String getWeekdayOperOpenHhmm() {
        return weekdayOperOpenHhmm;
    }

    public void setWeekdayOperOpenHhmm(String weekdayOperOpenHhmm) {
        this.weekdayOperOpenHhmm = weekdayOperOpenHhmm;
    }

    public String getWeekdayOperCloseHhmm() {
        return weekdayOperCloseHhmm;
    }

    public void setWeekdayOperCloseHhmm(String weekdayOperCloseHhmm) {
        this.weekdayOperCloseHhmm = weekdayOperCloseHhmm;
    }

    public String getSatOperOpenHhmm() {
        return satOperOpenHhmm;
    }

    public void setSatOperOpenHhmm(String satOperOpenHhmm) {
        this.satOperOpenHhmm = satOperOpenHhmm;
    }

    public String getSatOperCloseHhmm() {
        return satOperCloseHhmm;
    }

    public void setSatOperCloseHhmm(String satOperCloseHhmm) {
        this.satOperCloseHhmm = satOperCloseHhmm;
    }

    public String getHolidayOperOpenHhmm() {
        return holidayOperOpenHhmm;
    }

    public void setHolidayOperOpenHhmm(String holidayOperOpenHhmm) {
        this.holidayOperOpenHhmm = holidayOperOpenHhmm;
    }

    public String getHolidayOperCloseHhmm() {
        return holidayOperCloseHhmm;
    }

    public void setHolidayOperCloseHhmm(String holidayOperCloseHhmm) {
        this.holidayOperCloseHhmm = holidayOperCloseHhmm;
    }

    public String getParkingchrgeInfo() {
        return parkingchrgeInfo;
    }

    public void setParkingchrgeInfo(String parkingchrgeInfo) {
        this.parkingchrgeInfo = parkingchrgeInfo;
    }

    public String getBasicTime() {
        return basicTime;
    }

    public void setBasicTime(String basicTime) {
        this.basicTime = basicTime;
    }

    public String getBasicCharge() {
        return basicCharge;
    }

    public void setBasicCharge(String basicCharge) {
        this.basicCharge = basicCharge;
    }

    public String getAddUnitTime() {
        return addUnitTime;
    }

    public void setAddUnitTime(String addUnitTime) {
        this.addUnitTime = addUnitTime;
    }

    public String getAddUnitCharge() {
        return addUnitCharge;
    }

    public void setAddUnitCharge(String addUnitCharge) {
        this.addUnitCharge = addUnitCharge;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getParkingareaID() {
        return parkingareaID;
    }

    public void setParkingareaID(String parkingareaID) {
        this.parkingareaID = parkingareaID;
    }

    public ParkingClass() {}

    // 주차장 검색시 필요한 생성자
    public ParkingClass(String prkplceNm, String rdnmadr, String lnmadr, String latitude, String longitude, String prkplceNo) {
        this.prkplceNm = prkplceNm;
        this.rdnmadr = rdnmadr;
        this.lnmadr = lnmadr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prkplceNo = prkplceNo;
    }

    // 주차장 수정시 필요한 생성자
    public ParkingClass(String prkplceNm, String rdnmadr, String lnmadr, String prkplceNo) {
        this.prkplceNm = prkplceNm;
        this.rdnmadr = rdnmadr;
        this.lnmadr = lnmadr;
        this.prkplceNo = prkplceNo;
    }

    @Override
    public String toString() {
        // 주차장 검색시 필요한 생성자에 대한 정보
        if (prkplceNo == null) {
            return prkplceNm + "\n" + rdnmadr + "\n" + lnmadr;
        }
        // 주차장 수정시 필요한 생성자에 대한 정보
        else {
            return prkplceNm + "\n" + rdnmadr + "\n" + lnmadr;
        }
    }
}
