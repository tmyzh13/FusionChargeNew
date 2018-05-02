package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/1.
 */


public class ChargeDetailFeeListBean {

    private String chargePileId;
    private String endTime;
    private String feeList;
    private double multiFee;
    private String multiName;
    private String serviceFee;
    private String serviceName;
    private String startTime;
    public void setChargePileId(String chargePileId) {
        this.chargePileId = chargePileId;
    }
    public String getChargePileId() {
        return chargePileId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setFeeList(String feeList) {
        this.feeList = feeList;
    }
    public String getFeeList() {
        return feeList;
    }

    public void setMultiFee(double multiFee) {
        this.multiFee = multiFee;
    }
    public double getMultiFee() {
        return multiFee;
    }

    public void setMultiName(String multiName) {
        this.multiName = multiName;
    }
    public String getMultiName() {
        return multiName;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }
    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return serviceName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "ChargeDetailFeeListBean{" +
                "chargePileId='" + chargePileId + '\'' +
                ", endTime='" + endTime + '\'' +
                ", feeList='" + feeList + '\'' +
                ", multiFee=" + multiFee +
                ", multiName='" + multiName + '\'' +
                ", serviceFee='" + serviceFee + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}