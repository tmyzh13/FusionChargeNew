package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/4/23.
 */

public class MapInfoBean {

    public String address;
    public int freeNum;
    public String name;
    //类型
    public String objType;
    //对外开放
    public String openStatus;
    //桩数量
    public int pileNum;
    public int gunNum;
    public String averageScore;


    @Override
    public String toString() {
        return "MapInfoBean{" +
                "address='" + address + '\'' +
                ", freeNum=" + freeNum +
                ", name='" + name + '\'' +
                ", objType='" + objType + '\'' +
                ", openStatus='" + openStatus + '\'' +
                ", pileNum=" + pileNum +
                ", gunNum=" + gunNum +
                ", averageScore='" + averageScore + '\'' +
                '}';
    }
}
