package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/17.
 */

public class AppointTimeOutBean {

    private String gunCode;

    private int chargingPileId;

    private String chargingPileName;

    private double latitude;

    private double longitude;

    private String address;

    private String runCode;

    public String getGunCode() {
        return gunCode;
    }

    public void setGunCode(String gunCode) {
        this.gunCode = gunCode;
    }

    public int getChargingPileId() {
        return chargingPileId;
    }

    public void setChargingPileId(int chargingPileId) {
        this.chargingPileId = chargingPileId;
    }

    public String getChargingPileName() {
        return chargingPileName;
    }

    public void setChargingPileName(String chargingPileName) {
        this.chargingPileName = chargingPileName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    @Override
    public String toString() {
        return "AppointTimeOutBean{" +
                "gunCode='" + gunCode + '\'' +
                ", chargingPileId=" + chargingPileId +
                ", chargingPileName='" + chargingPileName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", runCode='" + runCode + '\'' +
                '}';
    }
}
