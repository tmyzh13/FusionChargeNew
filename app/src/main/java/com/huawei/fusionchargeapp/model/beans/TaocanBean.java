package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by john on 2018/5/8.
 */

public class TaocanBean {

    public double fee;
    public int id;
    public int limitCondition;
    public String startTime;
    public String endTime;
    public String name;
//    packageType 0：包月 1：包电量 2：包年无限
//    limitType 0：包电量 1：无限制
    public int packageType;

}
