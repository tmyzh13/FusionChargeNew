package com.huawei.fusionchargeapp.model.beans;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/1.
 */

public class ChargeDetailFeeBean {

    private int chargePileId;
    private List<ChargeDetailFeeListBean> feeList;
    private int serviceFee;
    private String serviceName;
    public void setChargePileId(int chargePileId) {
        this.chargePileId = chargePileId;
    }
    public int getChargePileId() {
        return chargePileId;
    }

    public void setFeeList(List<ChargeDetailFeeListBean> feeList) {
        this.feeList = feeList;
    }
    public List<ChargeDetailFeeListBean> getFeeList() {
        return feeList;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }
    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        return "ChargeDetailFeeBean{" +
                "chargePileId=" + chargePileId +
                ", feeList=" + feeList +
                ", serviceFee=" + serviceFee +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
