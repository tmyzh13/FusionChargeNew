package com.huawei.fusionchargeapp.model.beans;

/**
 * Created by issuser on 2018/4/23.
 */

public class MapDataBean {

    public String address;
    public long id;
    public double latitude;
    public double longitude;
    public int alterNum;
    public int directNum;
    public String title;
    //station pile
    public String type;
    public double distance;
    public int pileNum;

    @Override
    public String toString() {
        return "MapDataBean{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", alterNum=" + alterNum +
                ", directNum=" + directNum +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", distance=" + distance +
                '}';
    }
}
