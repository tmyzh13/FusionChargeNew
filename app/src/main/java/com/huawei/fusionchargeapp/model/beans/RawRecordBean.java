package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/4/28.
 */

public class RawRecordBean {



    private String chargTime;
    private String chargeEndTime;
    private String chargeStartTime;
    private double consumeTotalMoney;

    private String address;
    private int appUserId;
    private int chargePowerAmount;
    private String chargingTime;
    private double eneryCharge;
    private String gunCode;
    private int id;
    private String orderNum;
    private int payStatus;
    private String runCode;
    private double serviceCharge;
//    充电状态  '当前状态(0:充电中，1：充电结束，2：充电启动失败，3：充电停止失败)' 在未支付的状态下判断
    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getChargTime() {
        return chargTime;
    }

    public void setChargTime(String chargTime) {
        this.chargTime = chargTime;
    }

    public String getChargeEndTime() {
        return chargeEndTime;
    }

    public void setChargeEndTime(String chargeEndTime) {
        this.chargeEndTime = chargeEndTime;
    }

    public String getChargeStartTime() {
        return chargeStartTime;
    }

    public void setChargeStartTime(String chargeStartTime) {
        this.chargeStartTime = chargeStartTime;
    }

    public double getConsumeTotalMoney() {
        return consumeTotalMoney;
    }

    public void setConsumeTotalMoney(double consumeTotalMoney) {
        this.consumeTotalMoney = consumeTotalMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getChargePowerAmount() {
        return chargePowerAmount;
    }

    public void setChargePowerAmount(int chargePowerAmount) {
        this.chargePowerAmount = chargePowerAmount;
    }

    public String getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(String chargingTime) {
        this.chargingTime = chargingTime;
    }

    public double getEneryCharge() {
        return eneryCharge;
    }

    public void setEneryCharge(double eneryCharge) {
        this.eneryCharge = eneryCharge;
    }

    public String getGunCode() {
        return gunCode;
    }

    public void setGunCode(String gunCode) {
        this.gunCode = gunCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @Override
    public String toString() {
        return "RawRecordBean{" +
                "chargTime='" + chargTime + '\'' +
                ", chargeEndTime='" + chargeEndTime + '\'' +
                ", chargeStartTime='" + chargeStartTime + '\'' +
                ", consumeTotalMoney=" + consumeTotalMoney +
                ", address='" + address + '\'' +
                ", appUserId=" + appUserId +
                ", chargePowerAmount=" + chargePowerAmount +
                ", chargingTime='" + chargingTime + '\'' +
                ", eneryCharge=" + eneryCharge +
                ", gunCode='" + gunCode + '\'' +
                ", id=" + id +
                ", orderNum='" + orderNum + '\'' +
                ", payStatus=" + payStatus +
                ", runCode='" + runCode + '\'' +
                ", serviceCharge=" + serviceCharge +
                '}';
    }
}