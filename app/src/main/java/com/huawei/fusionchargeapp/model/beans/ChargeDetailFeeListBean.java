package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by zhangwei on 2018/5/1.
 */


public class ChargeDetailFeeListBean {

    private String endTime;
    private int multiFee;
    private String multiName;
    private String startTime;

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setMultiFee(int multiFee) {
        this.multiFee = multiFee;
    }
    public int getMultiFee() {
        return multiFee;
    }

    public void setMultiName(String multiName) {
        this.multiName = multiName;
    }
    public String getMultiName() {
        return multiName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }

}