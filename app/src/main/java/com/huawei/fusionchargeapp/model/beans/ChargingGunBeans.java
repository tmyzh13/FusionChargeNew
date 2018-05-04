package com.huawei.fusionchargeapp.model.beans;

import com.corelibs.utils.PreferencesHelper;

/**
 * Created by zhangwei on 2018/4/26.
 */

public class ChargingGunBeans {

    private int chargingPileId;
    private double currentA;
    private double currentB;
    private double currentC;
    private String gunCode;
    private int gunId;
    private String gunNumber;
    private int gunStatus;
    private int gunType;
    private int isBespeak;
    private String lockBeginTime;
    private String lockUser;
    private String parkingSpacesNumber;
    private double voltageA;
    private double voltageB;
    private double voltageC;
//    isReserve  0否 1是
    public int isReserve;
    public void setChargingPileId(int chargingPileId) {
        this.chargingPileId = chargingPileId;
    }
    public int getChargingPileId() {
        return chargingPileId;
    }

    public void setReserve(int reserve){
        this.isReserve=reserve;
    }

    public int getReserve(){
        return isReserve;
    }

    public void setCurrentA(int currentA) {
        this.currentA = currentA;
    }
    public double getCurrentA() {
        return currentA;
    }

    public void setCurrentB(int currentB) {
        this.currentB = currentB;
    }
    public double getCurrentB() {
        return currentB;
    }

    public void setCurrentC(int currentC) {
        this.currentC = currentC;
    }
    public double getCurrentC() {
        return currentC;
    }

    public void setGunCode(String gunCode) {
        this.gunCode = gunCode;
    }
    public String getGunCode() {
        return gunCode;
    }

    public void setGunId(int gunId) {
        this.gunId = gunId;
    }
    public int getGunId() {
        return gunId;
    }

    public void setGunNumber(String gunNumber) {
        this.gunNumber = gunNumber;
    }
    public String getGunNumber() {
        return gunNumber;
    }

    public void setGunStatus(int gunStatus) {
        this.gunStatus = gunStatus;
    }
    public int getGunStatus() {
        return gunStatus;
    }

    public void setGunType(int gunType) {
        this.gunType = gunType;
    }
    public int getGunType() {
        return gunType;
    }

    public void setIsBespeak(int isBespeak) {
        this.isBespeak = isBespeak;
    }
    public int getIsBespeak() {
        return isBespeak;
    }

    public void setLockBeginTime(String lockBeginTime) {
        this.lockBeginTime = lockBeginTime;
    }
    public String getLockBeginTime() {
        return lockBeginTime;
    }

    public void setLockUser(String lockUser) {
        this.lockUser = lockUser;
    }
    public String getLockUser() {
        return lockUser;
    }

    public void setParkingSpacesNumber(String parkingSpacesNumber) {
        this.parkingSpacesNumber = parkingSpacesNumber;
    }
    public String getParkingSpacesNumber() {
        return parkingSpacesNumber;
    }

    public void setVoltageA(int voltageA) {
        this.voltageA = voltageA;
    }
    public double getVoltageA() {
        return voltageA;
    }

    public void setVoltageB(int voltageB) {
        this.voltageB = voltageB;
    }
    public double getVoltageB() {
        return voltageB;
    }

    public void setVoltageC(int voltageC) {
        this.voltageC = voltageC;
    }
    public double getVoltageC() {
        return voltageC;
    }

    @Override
    public String toString() {
        return "ChargingGunBeans{" +
                "chargingPileId=" + chargingPileId +
                ", currentA=" + currentA +
                ", currentB=" + currentB +
                ", currentC=" + currentC +
                ", gunCode='" + gunCode + '\'' +
                ", gunId=" + gunId +
                ", gunNumber='" + gunNumber + '\'' +
                ", gunStatus=" + gunStatus +
                ", gunType=" + gunType +
                ", isBespeak=" + isBespeak +
                ", lockBeginTime='" + lockBeginTime + '\'' +
                ", lockUser='" + lockUser + '\'' +
                ", parkingSpacesNumber='" + parkingSpacesNumber + '\'' +
                ", voltageA=" + voltageA +
                ", voltageB=" + voltageB +
                ", voltageC=" + voltageC +
                '}';
    }
}
