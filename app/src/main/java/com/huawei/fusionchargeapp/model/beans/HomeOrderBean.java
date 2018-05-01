package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/4/24.
 */

public class HomeOrderBean {

    public String orderRecordNum;
    public long chargeId;
    public long id;
    public String chargeGunNum;

    @Override
    public String toString() {
        return "HomeOrderBean{" +
                "orderRecordNum='" + orderRecordNum + '\'' +
                ", chargeId=" + chargeId +
                ", id=" + id +
                ", chargeGunNum='" + chargeGunNum + '\'' +
                '}';
    }
}
