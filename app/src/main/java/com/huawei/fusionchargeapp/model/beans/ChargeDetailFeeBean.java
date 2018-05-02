package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/1.
 */

public class ChargeDetailFeeBean {

    private int chargePileId;
    private String endTime;
    private List<ChargeDetailFeeListBean> feeList;
    private String multiFee;
    private String multiName;
    private double serviceFee;
    private String serviceName;
    private String startTime;
    public void setChargePileId(int chargePileId) {
        this.chargePileId = chargePileId;
    }
    public int getChargePileId() {
        return chargePileId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setFeeList(List<ChargeDetailFeeListBean> feeList) {
        this.feeList = feeList;
    }
    public List<ChargeDetailFeeListBean> getFeeList() {
        return feeList;
    }

    public void setMultiFee(String multiFee) {
        this.multiFee = multiFee;
    }
    public String getMultiFee() {
        return multiFee;
    }

    public void setMultiName(String multiName) {
        this.multiName = multiName;
    }
    public String getMultiName() {
        return multiName;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }
    public double getServiceFee() {
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
}
